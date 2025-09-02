package io.v4guard.shield.bungee.listener;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.redis.UserStateUpdateRedisMessage;
import io.v4guard.shield.common.redis.type.OperationType;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class BungeeRedisBungeeListener implements Listener {
    private final ShieldBungee plugin;
    private final RedisBungeeConnectedCounterService redisBungeeListener;

    public BungeeRedisBungeeListener(ShieldBungee plugin, RedisBungeeConnectedCounterService redisBungeeListener) {
        this.plugin = plugin;
        this.redisBungeeListener = redisBungeeListener;
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        InetAddress ip = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress();

        RedisBungeeAPI.getRedisBungeeApi().sendChannelMessage(
                "accshield",
                createMessage(OperationType.JOIN,
                        event.getPlayer().getName(),
                        ip
                ));

        redisBungeeListener.add(ip, event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        InetAddress ip = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress();

        RedisBungeeAPI.getRedisBungeeApi().sendChannelMessage(
                "accshield",
                createMessage(OperationType.JOIN,
                        event.getPlayer().getName(),
                        ip
                ));

        redisBungeeListener.remove(ip, event.getPlayer().getName());
    }

    @EventHandler
    public void onMessage(PubSubMessageEvent event) {
        if (!event.getChannel().equals("accshield")) return;

        redisBungeeListener.handleMessage(event.getMessage());
    }

    private String createMessage(OperationType type, String name, InetAddress ip) {
        UserStateUpdateRedisMessage message = new UserStateUpdateRedisMessage(
                type, ip, name
        );
        try {
            return plugin.getCommon().getObjectMapper().writeValueAsString(message);
        } catch (Exception e) {
            return "";
        }
    }

}
