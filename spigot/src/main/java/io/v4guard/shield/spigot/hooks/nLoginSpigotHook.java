package io.v4guard.shield.spigot.hooks;

import com.nickuc.login.api.event.bukkit.auth.LoginEvent;
import com.nickuc.login.api.event.bukkit.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.bukkit.auth.RegisterEvent;
import com.nickuc.login.api.event.bukkit.auth.WrongPasswordEvent;

import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.spigot.ShieldSpigot;
import io.v4guard.shield.spigot.model.AuthType;
import io.v4guard.shield.spigot.model.Authentication;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class nLoginSpigotHook extends AuthenticationHook implements Listener {

    private final ShieldSpigot plugin;

    public nLoginSpigotHook(ShieldSpigot plugin) {
        super("nLogin");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPremiumLogin(PremiumLoginEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.MOJANG,
                event.getPlayer().hasPermission("v4guard.accshield")
        );

        plugin.sendPluginMessage(auth);
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
    public void onWrongPassword(WrongPasswordEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.WRONG,
                event.getPlayer().hasPermission("v4guard.accshield")
        );

        plugin.sendPluginMessage(auth);
    }
}
