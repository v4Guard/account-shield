package io.v4guard.shield.bungee.hooks;

import com.nickuc.login.api.event.bungee.auth.LoginEvent;
import com.nickuc.login.api.event.bungee.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.bungee.auth.RegisterEvent;
import com.nickuc.login.api.event.bungee.auth.WrongPasswordEvent;
import io.v4guard.connector.common.accounts.auth.AuthType;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.common.hook.AuthenticationHook;
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

        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.MOJANG,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);

    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.LOGIN,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.REGISTER,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

    @EventHandler
    public void onWrongPassword(WrongPasswordEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        Authentication auth = new Authentication(
                proxiedPlayer.getName(),
                proxiedPlayer.getUniqueId(),
                AuthType.WRONG,
                proxiedPlayer.hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
    }

}
