package io.v4guard.shield.velocity.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.redis.UserStateUpdateRedisMessage;
import io.v4guard.shield.common.redis.type.OperationType;
import io.v4guard.shield.velocity.ShieldVelocity;

import java.net.InetAddress;

public class VelocityRedisBungeeListener {
    private final ShieldVelocity plugin;
    private final RedisBungeeConnectedCounterService redisBungeeListener;
    private final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

    public VelocityRedisBungeeListener(ShieldVelocity plugin, RedisBungeeConnectedCounterService redisBungeeListener) {
        this.plugin = plugin;
        this.redisBungeeListener = redisBungeeListener;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        redisBungeeAPI.sendChannelMessage(
                "accshield",
                createMessage(OperationType.JOIN,
                        event.getPlayer().getUsername(),
                        event.getPlayer().getRemoteAddress().getAddress()
                )
        );

        redisBungeeListener.add(event.getPlayer().getRemoteAddress().getAddress(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        redisBungeeAPI.sendChannelMessage(
                "accshield",
                createMessage(OperationType.QUIT,
                        event.getPlayer().getUsername(),
                        event.getPlayer().getRemoteAddress().getAddress()
                ));
        redisBungeeListener.remove(event.getPlayer().getRemoteAddress().getAddress(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void onMessage(PubSubMessageEvent event) {
        if (!event.getChannel().equals("accshield")) return;

        // Delegate the handling of the message to our common implementation
        redisBungeeListener.handleMessage(event.getMessage());
    }

    private String createMessage(OperationType type, String name, InetAddress ip) {
        UserStateUpdateRedisMessage message = new UserStateUpdateRedisMessage(
                type, ip, name
        );
        try {
            return plugin.getCommon().getObjectMapper().writeValueAsString(message);
        } catch (Exception e) {
            plugin.getLogger().error("Failed to create message for RedisBungee", e);
            return "";
        }
    }

}