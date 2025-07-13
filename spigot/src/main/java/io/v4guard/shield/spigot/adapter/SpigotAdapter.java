package io.v4guard.shield.spigot.adapter;

import io.v4guard.shield.api.adapter.PlayerAdapter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotAdapter implements PlayerAdapter {

    private final Player player;

    public SpigotAdapter(Player player) {
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
