package io.v4guard.shield.spigot.hooks;

import com.nickuc.login.api.event.bukkit.auth.LoginEvent;
import com.nickuc.login.api.event.bukkit.auth.RegisterEvent;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class nLoginSpigotHook extends AuthenticationHook implements Listener {

    public nLoginSpigotHook(JavaPlugin plugin) {
        super("nLogin");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getServer().getConsoleSender().sendMessage("§c[v4guard-account-shield] (Spigot) Hooked into nLogin");
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        if(!event.getPlayer().hasPermission("v4guard.accshield")) return;
        Authentication auth = new Authentication(event.getPlayer().getName(), AuthType.LOGIN);
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        if(!event.getPlayer().hasPermission("v4guard.accshield")) return;
        Authentication auth = new Authentication(event.getPlayer().getName(), AuthType.REGISTER);
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

}
