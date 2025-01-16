package io.v4guard.shield.bungee.service;

import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.bungee.ShieldBungee;
import net.md_5.bungee.api.CommandSender;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeConnectedCountService  implements ConnectedCounterService {

    private final ShieldBungee plugin;

    public BungeeConnectedCountService(ShieldBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasAnyAccountOnline(InetAddress ipAddress) {
        return this.plugin.getProxy().getPlayers().stream().anyMatch(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().equals(ipAddress));
    }

    @Override
    public boolean hasAnyAccountOnline(String ipAddress) {
        return this.plugin.getProxy().getPlayers().stream().anyMatch(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().getHostAddress().equals(ipAddress));
    }

    @Override
    public int getConnectedAccounts(InetAddress ipAddress) {
        return (int) this.plugin.getProxy().getPlayers().stream().filter(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().equals(ipAddress)).count();
    }

    @Override
    public int getConnectedAccounts(String ipAddress) {
        return (int) this.plugin.getProxy().getPlayers().stream().filter(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().getHostAddress().equals(ipAddress)).count();
    }

    @Override
    public Collection<String> getConnectedAccounts(InetAddress ipAddress, int limit) {
        return this.plugin.getProxy()
                .getPlayers()
                .stream()
                .filter(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().equals(ipAddress))
                .map(CommandSender::getName)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getConnectedAccounts(String ipAddress, int limit) {
        return this.plugin.getProxy()
                .getPlayers()
                .stream()
                .filter(player -> ((InetSocketAddress) player.getSocketAddress()).getAddress().getHostAddress().equals(ipAddress))
                .map(CommandSender::getName)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
