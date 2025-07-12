package io.v4guard.shield.spigot.hooks;

import com.nickuc.login.api.event.bukkit.auth.LoginEvent;
import com.nickuc.login.api.event.bukkit.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.bukkit.auth.RegisterEvent;
import com.nickuc.login.api.event.bukkit.auth.WrongPasswordEvent;

import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.spigot.ShieldSpigot;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.spigot.adapter.SpigotAdapter;
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
        PlayerAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication auth = prepareAuthentication(player, AuthType.MOJANG);
        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        PlayerAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication auth = prepareAuthentication(player, AuthType.LOGIN);
        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        PlayerAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication auth = prepareAuthentication(player, AuthType.REGISTER);
        plugin.sendAuthenticationData(auth);
    }

    @EventHandler
    public void onWrongPassword(WrongPasswordEvent event) {
        PlayerAdapter player = new SpigotAdapter(event.getPlayer());
        Authentication auth = prepareAuthentication(player, AuthType.WRONG);
        plugin.sendAuthenticationData(auth);
    }
}
