package io.v4guard.shield.velocity.service;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.v4guard.shield.api.service.ConnectedCounterService;

import java.net.InetAddress;
import java.util.Collection;
import java.util.stream.Collectors;

public class VelocityConnectedCounterService implements ConnectedCounterService {

    private final ProxyServer server;

    public VelocityConnectedCounterService(ProxyServer server) {
        this.server = server;
    }

    @Override
    public boolean hasAnyAccountOnline(InetAddress ipAddress) {
        return this.server.getAllPlayers().stream().anyMatch(player -> player.getRemoteAddress().getAddress().equals(ipAddress));
    }

    @Override
    public boolean hasAnyAccountOnline(String ipAddress) {
        return this.server.getAllPlayers().stream().anyMatch(player -> player.getRemoteAddress().getAddress().getHostAddress().equals(ipAddress));
    }

    @Override
    public int getConnectedAccounts(InetAddress ipAddress) {
        return (int) this.server.getAllPlayers().stream().filter(player -> player.getRemoteAddress().getAddress().equals(ipAddress)).count();
    }

    @Override
    public int getConnectedAccounts(String ipAddress) {
        return (int) this.server.getAllPlayers().stream().filter(player -> player.getRemoteAddress().getAddress().getHostAddress().equals(ipAddress)).count();
    }

    @Override
    public Collection<String> getConnectedAccounts(InetAddress ipAddress, int limit) {
        return this.server.getAllPlayers()
                .stream()
                .filter(player -> player.getRemoteAddress().getAddress().equals(ipAddress))
                .map(Player::getUsername)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getConnectedAccounts(String ipAddress, int limit) {
        return this.server.getAllPlayers()
                .stream()
                .filter(player -> player.getRemoteAddress().getAddress().getHostAddress().equals(ipAddress))
                .map(Player::getUsername)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
