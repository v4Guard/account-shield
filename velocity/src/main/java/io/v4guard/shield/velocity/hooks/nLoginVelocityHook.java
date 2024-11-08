package io.v4guard.shield.velocity.hooks;

import com.nickuc.login.api.event.velocity.auth.LoginEvent;
import com.nickuc.login.api.event.velocity.auth.RegisterEvent;
import com.nickuc.login.api.event.velocity.auth.WrongPasswordEvent;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import io.v4guard.connector.common.UnifiedLogger;
import io.v4guard.connector.common.accounts.auth.AuthType;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.velocity.ShieldVelocity;

import java.util.logging.Level;

public class nLoginVelocityHook extends AuthenticationHook {

    private final ShieldVelocity plugin;

    public nLoginVelocityHook(ShieldVelocity plugin) {
        super("nLogin");
        this.plugin = plugin;
        UnifiedLogger.get().log(Level.INFO, "Hooked into nLogin");
    }

    @Subscribe
    public void onLogin(LoginEvent event, Continuation continuation) {
        Authentication auth = new Authentication(
                event.getPlayer().getUsername(),
                event.getPlayer().getUniqueId(),
                AuthType.LOGIN,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        plugin.getCommon().sendMessage(auth);

        continuation.resume();
    }

    @Subscribe
    public void onRegister(RegisterEvent event, Continuation continuation) {
        Authentication auth = new Authentication(
                event.getPlayer().getUsername(),
                event.getPlayer().getUniqueId(),
                AuthType.REGISTER,
                event.getPlayer().hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
        continuation.resume();
    }

    @Subscribe
    public void onWrongPassword(WrongPasswordEvent event, Continuation continuation) {
        Authentication auth = new Authentication(
                event.getPlayer().getUsername(),
                event.getPlayer().getUniqueId(),
                AuthType.WRONG,
                event.getPlayer().hasPermission("v4guard.accshield")
        );

        plugin.getCommon().sendMessage(auth);
        continuation.resume();
    }

}
