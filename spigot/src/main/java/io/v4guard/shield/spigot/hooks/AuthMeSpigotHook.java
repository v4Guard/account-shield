package io.v4guard.shield.spigot.hooks;

import fr.xephi.authme.events.FailedLoginEvent;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.spigot.ShieldSpigot;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.spigot.adapter.SpigotAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeSpigotHook extends AuthenticationHook implements Listener {

    private final ShieldSpigot plugin;

    public AuthMeSpigotHook(ShieldSpigot plugin) {
        super("AuthMe");
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        SpigotAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication authentication = prepareAuthentication(player, AuthType.LOGIN);
        plugin.sendAuthenticationData(authentication);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        SpigotAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication authentication = prepareAuthentication(player, AuthType.REGISTER);
        plugin.sendAuthenticationData(authentication);
    }


    @EventHandler
    public void onWrongPassword(FailedLoginEvent event) {
        SpigotAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication authentication = prepareAuthentication(player, AuthType.WRONG);
        plugin.sendAuthenticationData(authentication);
    }
}
