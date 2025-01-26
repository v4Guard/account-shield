package io.v4guard.shield.velocity.listener;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PlayerJoinedNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.events.PlayerLeftNetworkEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;

import java.net.InetAddress;

public class VelocityRedisBungeeListener {

    private final RedisBungeeConnectedCounterService redisBungeeListener;
    private final RedisBungeeAPI redisBungeeAPI = RedisBungeeAPI.getRedisBungeeApi();

    public VelocityRedisBungeeListener(RedisBungeeConnectedCounterService redisBungeeListener) {
        this.redisBungeeListener = redisBungeeListener;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        redisBungeeListener.add(event.getPlayer().getRemoteAddress().getAddress());
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        redisBungeeListener.remove(event.getPlayer().getRemoteAddress().getAddress());
    }

    @Subscribe
    public void onRedisBungeePlayerJoin(PlayerJoinedNetworkEvent event) {
        InetAddress address = redisBungeeAPI.getPlayerIp(event.getUuid());
        if (address == null) return;

        redisBungeeListener.add(address);
    }

    @Subscribe
    public void onRedisBungeePlayerQuit(PlayerLeftNetworkEvent event) {
        InetAddress address = redisBungeeAPI.getPlayerIp(event.getUuid());
        if (address == null) return;
        redisBungeeListener.remove(address);
    }

}
