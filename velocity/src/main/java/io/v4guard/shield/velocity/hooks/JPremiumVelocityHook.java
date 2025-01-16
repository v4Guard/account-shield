package io.v4guard.shield.velocity.hooks;

import com.jakub.jpremium.proxy.api.event.velocity.UserEvent;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import io.v4guard.connector.common.accounts.auth.AuthType;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.velocity.ShieldVelocity;

import java.util.Optional;

public class JPremiumVelocityHook extends AuthenticationHook {

    private final ShieldVelocity plugin;

    public JPremiumVelocityHook(ShieldVelocity plugin) {
        super("jpremium");
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerLogin(UserEvent.Login event, Continuation continuation) {
        Optional<Player> player = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        player.ifPresent(p -> {
            Authentication auth = new Authentication(
                    p.getUsername(),
                    p.getUniqueId(),
                    event.getUserProfile().isPremium() ? AuthType.MOJANG : AuthType.LOGIN,
                    p.hasPermission("v4guard.accshield")
            );

            plugin.getCommon().sendMessage(auth);
        });

        continuation.resume();
    }

    @Subscribe
    public void onPlayerRegister(UserEvent.Register event, Continuation continuation) {
        Optional<Player> player = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        player.ifPresent(p -> {
            Authentication auth = new Authentication(
                    p.getUsername(),
                    p.getUniqueId(),
                    AuthType.REGISTER,
                    p.hasPermission("v4guard.accshield")
            );
            plugin.getCommon().sendMessage(auth);
        });

        continuation.resume();
    }

    @Subscribe
    public void onPlayerWrongPassword(UserEvent.FailedLogin event, Continuation continuation) {
        Optional<Player> player = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        player.ifPresent(p -> {
            Authentication auth = new Authentication(
                    p.getUsername(),
                    p.getUniqueId(),
                    AuthType.WRONG,
                    p.hasPermission("v4guard.accshield")
            );
            plugin.getCommon().sendMessage(auth);
        });

        continuation.resume();

    }
}
