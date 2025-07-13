package io.v4guard.shield.bungee.adapter;

import io.v4guard.shield.api.adapter.PlayerAdapter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeePlayerAdapter implements PlayerAdapter {

    private final ProxiedPlayer player;

    public BungeePlayerAdapter(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
