package io.v4guard.shield.bungee.hooks;

import com.nickuc.login.api.event.bungee.auth.LoginEvent;
import com.nickuc.login.api.event.bungee.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.bungee.auth.RegisterEvent;
import com.nickuc.login.api.event.bungee.auth.WrongPasswordEvent;
import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.bungee.adapter.BungeePlayerAdapter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class nLoginBungeeHook extends AuthenticationHook implements Listener {

    private final ShieldBungee plugin;

    public nLoginBungeeHook(ShieldBungee plugin) {
        super("nLogin");
        this.plugin = plugin;
    }

    @EventHandler
    public void onPremiumLogin(PremiumLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);
        Authentication auth = prepareAuthentication(
                player,
                AuthType.MOJANG
        );

        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);
        Authentication auth = prepareAuthentication(
                player,
                AuthType.LOGIN
        );

        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);
        Authentication auth = prepareAuthentication(
                player,
                AuthType.REGISTER
        );

        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onWrongPassword(WrongPasswordEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        PlayerAdapter player = new BungeePlayerAdapter(proxiedPlayer);
        Authentication auth = prepareAuthentication(
                player,
                AuthType.WRONG
        );

        plugin.sendAuthenticationData(auth);
    }

}
