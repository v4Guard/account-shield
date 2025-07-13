package io.v4guard.shield.velocity.adapter;

import com.velocitypowered.api.proxy.Player;
import io.v4guard.shield.api.adapter.PlayerAdapter;

import java.util.UUID;

public class VelocityPlayerAdapter implements PlayerAdapter {

    private final Player player;

    public VelocityPlayerAdapter(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getUsername();
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
