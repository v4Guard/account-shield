package io.v4guard.shield.api.adapter;

import java.util.UUID;

/**
 * An adapter for a player in their corresponding{@link io.v4guard.shield.api.platform.ShieldPlatform platform}
 */
public interface PlayerAdapter {

    /**
     * Get the player's name
     * @return the player's name
     */
    String getName();

    /**
     * Get the player's unique id
     * @return the player's unique id
     */
    UUID getUniqueId();

    /**
     * Check if the player has a specific permission
     * @param permission the permission to check
     * @return true if the player has the permission, false otherwise
     */
    boolean hasPermission(String permission);
}
