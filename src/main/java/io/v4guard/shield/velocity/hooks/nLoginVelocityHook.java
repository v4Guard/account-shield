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
        if(!event.getPlayer().hasPermission("v4guard.accshield")) return;
        Authentication auth = new Authentication(event.getPlayer().getUsername(), AuthType.LOGIN);
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

    @Subscribe
    public void onRegister(RegisterEvent event) {
        if(!event.getPlayer().hasPermission("v4guard.accshield")) return;
        Authentication auth = new Authentication(event.getPlayer().getUsername(), AuthType.REGISTER);
        v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
    }

}
