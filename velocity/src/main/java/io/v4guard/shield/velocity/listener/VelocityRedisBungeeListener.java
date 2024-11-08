package io.v4guard.shield.velocity.listener;

import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.imaginarycode.minecraft.redisbungee.events.PlayerJoinedNetworkEvent;
import com.imaginarycode.minecraft.redisbungee.events.PlayerLeftNetworkEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.v4guard.shield.common.api.RedisBungeeConnectedCounterService;

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
        redisBungeeListener.add(redisBungeeAPI.getPlayerIp(event.getUuid()));
    }

    @Subscribe
    public void onRedisBungeePlayerQuit(PlayerLeftNetworkEvent event) {
        redisBungeeListener.remove(redisBungeeAPI.getPlayerIp(event.getUuid()));
    }

}
