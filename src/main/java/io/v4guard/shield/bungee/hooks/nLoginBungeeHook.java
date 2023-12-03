package io.v4guard.shield.bungee.hooks;

import com.nickuc.login.api.event.bungee.auth.LoginEvent;
import com.nickuc.login.api.event.bungee.auth.RegisterEvent;
import com.nickuc.login.api.event.bungee.auth.WrongPasswordEvent;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class nLoginBungeeHook extends AuthenticationHook implements Listener {

    public nLoginBungeeHook(Plugin plugin) {
        super("nLogin");
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
        plugin.getProxy().getConsole().sendMessage("§c[v4guard-account-shield] (Bungee) Hooked into nLogin");
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.LOGIN,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.REGISTER,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @EventHandler
    public void onWrongPassword(WrongPasswordEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getName(),
                event.getPlayer().getUniqueId(),
                AuthType.FAILED,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }
}
