package io.v4guard.shield.velocity.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.velocity.ShieldVelocity;

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
                getMessage("join",
                        event.getPlayer().getUsername(),
                        event.getPlayer().getRemoteAddress().getAddress().getHostAddress()
                ));

        redisBungeeListener.add(event.getPlayer().getRemoteAddress().getAddress(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        redisBungeeAPI.sendChannelMessage(
                "accshield",
                getMessage("quit",
                        event.getPlayer().getUsername(),
                        event.getPlayer().getRemoteAddress().getAddress().getHostAddress()
                ));
        redisBungeeListener.remove(event.getPlayer().getRemoteAddress().getAddress(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void onMessage(PubSubMessageEvent event) {
        if (event.getChannel().equals("accshield")) {
            redisBungeeListener.handleMessage(event.getMessage());
        }
    }

    private String getMessage(String type, String name, String ip) {
        ObjectNode data = plugin.getCommon().getObjectMapper().createObjectNode();
        data.put("type", type);
        data.put("name", name);
        data.put("ip", ip);
        return data.toString();
    }

}
