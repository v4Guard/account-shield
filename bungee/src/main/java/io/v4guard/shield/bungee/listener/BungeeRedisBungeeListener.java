package io.v4guard.shield.bungee.listener;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class BungeeRedisBungeeListener implements Listener {
    private final ShieldBungee plugin;
    private final RedisBungeeConnectedCounterService redisBungeeListener;
    private final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

    public BungeeRedisBungeeListener(ShieldBungee plugin, RedisBungeeConnectedCounterService redisBungeeListener) {
        this.plugin = plugin;
        this.redisBungeeListener = redisBungeeListener;
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        InetAddress ip = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress();

        redisBungeeAPI.sendChannelMessage(
                "accshield",
                getMessage("join",
                        event.getPlayer().getName(),
                        ip.getHostAddress()
                ));
        redisBungeeListener.add(ip, event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        InetAddress ip = ((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress();

        redisBungeeAPI.sendChannelMessage(
                "accshield",
                getMessage("quit",
                        event.getPlayer().getName(),
                        ip.getHostAddress()
                ));
        redisBungeeListener.remove(ip, event.getPlayer().getName());
    }

    @EventHandler
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
