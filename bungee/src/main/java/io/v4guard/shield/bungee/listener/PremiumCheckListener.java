package io.v4guard.shield.bungee.listener;

import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.bungee.ShieldBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PremiumCheckListener implements Listener {

    // This event is used on the case there is no hooks. Check if the user is premium by the proxy #isOnline method (may vary by proxy type)
    private final ShieldBungee plugin;

    public PremiumCheckListener(ShieldBungee shieldBungee) {
        this.plugin = shieldBungee;
    }

    @EventHandler
    public void onPremiumCheck(PostLoginEvent event) {
        if (plugin.getActiveAuthenticationHook() != null) return;
        ProxiedPlayer player = event.getPlayer();

        if (!player.getPendingConnection().isOnlineMode()) return;

        Authentication authentication = new Authentication(
                player.getName(),
                player.getUniqueId(),
                AuthType.MOJANG,
                player.hasPermission("v4guard.accshield")
        );

        plugin.sendAuthenticationData(authentication);
    }
}
