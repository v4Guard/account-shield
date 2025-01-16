package io.v4guard.shield.spigot.hooks;

import fr.xephi.authme.events.FailedLoginEvent;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.spigot.ShieldSpigot;
import io.v4guard.shield.spigot.model.AuthType;
import io.v4guard.shield.spigot.model.Authentication;
import org.bukkit.Bukkit;
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
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.LOGIN,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        plugin.sendPluginMessage(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.REGISTER,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        plugin.sendPluginMessage(auth);
    }


    @EventHandler
    public void onWrongPassword(FailedLoginEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.WRONG,
                event.getPlayer().hasPermission("v4guard.accshield")
        );

        plugin.sendPluginMessage(auth);
    }
}
