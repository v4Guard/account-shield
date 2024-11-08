package io.v4guard.shield.bungee.listener;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PlayerJoinedNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.events.PlayerLeftNetworkEvent;
import io.v4guard.shield.common.api.RedisBungeeConnectedCounterService;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public class BungeeRedisBungeeListener implements Listener {

    private final RedisBungeeConnectedCounterService redisBungeeListener;
    private final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

    public BungeeRedisBungeeListener(RedisBungeeConnectedCounterService redisBungeeListener) {
        this.redisBungeeListener = redisBungeeListener;
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        redisBungeeListener.add(((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress());
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        redisBungeeListener.remove(((InetSocketAddress) event.getPlayer().getSocketAddress()).getAddress());
    }

    @EventHandler
    public void onRedisBungeePlayerJoin(PlayerJoinedNetworkEvent event) {
        redisBungeeListener.add(redisBungeeAPI.getPlayerIp(event.getUuid()));
    }

    @EventHandler
    public void onRedisBungeePlayerQuit(PlayerLeftNetworkEvent event) {
        redisBungeeListener.remove(redisBungeeAPI.getPlayerIp(event.getUuid()));
    }
}
