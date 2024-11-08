package io.v4guard.shield.bungee.hooks;

import com.jakub.jpremium.proxy.api.event.bungee.UserEvent;
import io.v4guard.connector.common.accounts.auth.AuthType;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.common.hook.AuthenticationHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
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

        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.LOGIN,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

    @EventHandler
    public void onUserEvent(UserEvent.Register event) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(event.getUserProfile().getUniqueId());
        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.REGISTER,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

    @EventHandler
    public void onUserEvent(UserEvent.FailedLogin event) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(event.getUserProfile().getUniqueId());
        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.WRONG,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

}
