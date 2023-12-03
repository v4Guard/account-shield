package io.v4guard.shield.bungee.hooks;

import com.jakub.premium.api.event.UserEvent;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class JPremiumBungeeHook extends AuthenticationHook implements Listener {

    public JPremiumBungeeHook(Plugin plugin) {
        super("JPremium");
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
        plugin.getProxy().getConsole().sendMessage("§c[v4guard-account-shield] (Bungee) Hooked into JPremium");
    }

    @EventHandler
    public void onUserEvent(UserEvent.Login event) {
        ProxiedPlayer proxiedPlayer = event.getUserProfile().getProxiedPlayer();
        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.LOGIN,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @EventHandler
    public void onUserEvent(UserEvent.Register event) {
        ProxiedPlayer proxiedPlayer = event.getUserProfile().getProxiedPlayer();
        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.REGISTER,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

}
