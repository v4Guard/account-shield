package io.v4guard.shield.velocity.hooks;

import com.jakub.jpremium.proxy.api.event.velocity.UserEvent;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.velocity.ShieldVelocity;
import io.v4guard.shield.velocity.adapter.VelocityPlayerAdapter;

import java.util.Optional;

public class JPremiumVelocityHook extends AuthenticationHook {

    private final ShieldVelocity plugin;

    public JPremiumVelocityHook(ShieldVelocity plugin) {
        super("jpremium");
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerLogin(UserEvent.Login event, Continuation continuation) {
        Optional<Player> foundPlayer = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        foundPlayer.ifPresent(p -> {
            PlayerAdapter player = new VelocityPlayerAdapter(p);
            Authentication auth = prepareAuthentication(
                    player,
                    event.getUserProfile().isPremium() ? AuthType.MOJANG : AuthType.LOGIN
            );

            plugin.sendAuthenticationData(auth);
        });

        continuation.resume();
    }

    @Subscribe
    public void onPlayerRegister(UserEvent.Register event, Continuation continuation) {
        Optional<Player> foundPlayer = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        foundPlayer.ifPresent(p -> {
            PlayerAdapter player = new VelocityPlayerAdapter(p);
            Authentication auth = prepareAuthentication(
                    player,
                    AuthType.REGISTER
            );

            plugin.sendAuthenticationData(auth);
        });

        continuation.resume();
    }

    @Subscribe
    public void onPlayerWrongPassword(UserEvent.FailedLogin event, Continuation continuation) {
        Optional<Player> foundPlayer = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());

        foundPlayer.ifPresent(p -> {
            PlayerAdapter player = new VelocityPlayerAdapter(p);
            Authentication auth = prepareAuthentication(
                    player,
                    AuthType.WRONG
            );

            plugin.sendAuthenticationData(auth);
        });

        continuation.resume();

    }
}
