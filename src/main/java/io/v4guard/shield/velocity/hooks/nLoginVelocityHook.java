package io.v4guard.shield.velocity.hooks;

import com.nickuc.login.api.event.velocity.auth.LoginEvent;
import com.nickuc.login.api.event.velocity.auth.RegisterEvent;
import com.velocitypowered.api.event.Subscribe;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import io.v4guard.shield.velocity.v4GuardShieldVelocity;
import net.md_5.bungee.api.plugin.Listener;

public class nLoginVelocityHook extends AuthenticationHook implements Listener {

    public nLoginVelocityHook(v4GuardShieldVelocity plugin) {
        super("nLogin");
        plugin.getServer().getEventManager().register(plugin, this);
        plugin.sendConsoleMessage("§c[v4guard-account-shield] (Velocity) Hooked into nLogin");
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getUsername(),
                event.getPlayer().getUniqueId(),
                AuthType.LOGIN,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @Subscribe
    public void onRegister(RegisterEvent event) {
        Authentication auth = new Authentication(
                event.getPlayer().getUsername(),
                event.getPlayer().getUniqueId(),
                AuthType.REGISTER,
                event.getPlayer().hasPermission("v4guard.accshield")
        );
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

}
