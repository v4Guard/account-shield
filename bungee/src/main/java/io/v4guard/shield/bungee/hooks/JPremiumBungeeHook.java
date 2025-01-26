package io.v4guard.shield.bungee.hooks;

import com.jakub.jpremium.proxy.api.event.bungee.UserEvent;
import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.bungee.adapter.BungeePlayerAdapter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JPremiumBungeeHook extends AuthenticationHook implements Listener {

    private final ShieldBungee plugin;

    public JPremiumBungeeHook(ShieldBungee plugin) {
        super("jPremium");
        this.plugin = plugin;
    }


    @EventHandler
    public void onUserEvent(UserEvent.Login event) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(event.getUserProfile().getUniqueId());
        if (proxiedPlayer == null) return;

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);

        Authentication auth = prepareAuthentication(
                player,
                AuthType.LOGIN
        );

        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onUserEvent(UserEvent.Register event) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(event.getUserProfile().getUniqueId());
        if (proxiedPlayer == null) return;

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);

        Authentication auth = prepareAuthentication(
                player,
                AuthType.REGISTER
        );

        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onUserEvent(UserEvent.FailedLogin event) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(event.getUserProfile().getUniqueId());
        if (proxiedPlayer == null) return;

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);

        Authentication auth = prepareAuthentication(
                player,
                AuthType.WRONG
        );

        plugin.sendAuthenticationData(auth);
    }

}
