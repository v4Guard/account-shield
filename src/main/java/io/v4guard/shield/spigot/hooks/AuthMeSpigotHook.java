package io.v4guard.shield.spigot.hooks;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import io.v4guard.shield.spigot.v4GuardShieldSpigot;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthMeSpigotHook extends AuthenticationHook implements Listener {

    public AuthMeSpigotHook(JavaPlugin plugin) {
        super("AuthMe");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getConsoleSender().sendMessage("§c[v4guard-account-shield] (Spigot) Hooked into AuthMe");
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                AuthType.LOGIN,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                AuthType.REGISTER,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

}
