package io.v4guard.shield.velocity.listener;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.velocity.ShieldVelocity;

public class PremiumCheckListener {

    // This event is used on the case there is no hooks. Check if the user is premium by the proxy #isOnline method (may vary by proxy type)
    private final ShieldVelocity plugin;

    public PremiumCheckListener(ShieldVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPremiumCheck(PostLoginEvent event, Continuation continuation) {
        if (plugin.getActiveAuthenticationHook() != null) {
            continuation.resume();
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOnlineMode()) {
            continuation.resume();
            return;
        }

        // Delegate the check to the active hook
        Authentication auth = new Authentication(
                player.getUsername(),
                player.getUniqueId(),
                AuthType.MOJANG,
                player.hasPermission("v4guard.accshield")
        );

        plugin.sendAuthenticationData(auth);
        continuation.resume();
    }
}
