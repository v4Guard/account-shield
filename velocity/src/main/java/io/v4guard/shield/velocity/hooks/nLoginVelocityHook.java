package io.v4guard.shield.velocity.hooks;

import com.nickuc.login.api.event.velocity.auth.LoginEvent;
import com.nickuc.login.api.event.velocity.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.velocity.auth.RegisterEvent;
import com.nickuc.login.api.event.velocity.auth.WrongPasswordEvent;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.velocity.ShieldVelocity;
import io.v4guard.shield.velocity.adapter.VelocityPlayerAdapter;


public class nLoginVelocityHook extends AuthenticationHook {

    private final ShieldVelocity plugin;

    public nLoginVelocityHook(ShieldVelocity plugin) {
        super("nLogin");
        this.plugin = plugin;
    }

    @Subscribe
    public void onPremiumLogin(PremiumLoginEvent event, Continuation continuation) {
        PlayerAdapter player = new VelocityPlayerAdapter(event.getPlayer());

        Authentication authentication = prepareAuthentication(player, AuthType.MOJANG);
        plugin.sendAuthenticationData(authentication);

        continuation.resume();
    }

    @Subscribe
    public void onLogin(LoginEvent event, Continuation continuation) {
        PlayerAdapter player = new VelocityPlayerAdapter(event.getPlayer());

        Authentication auth = prepareAuthentication(player, AuthType.LOGIN);
        plugin.sendAuthenticationData(auth);

        continuation.resume();
    }

    @Subscribe
    public void onRegister(RegisterEvent event, Continuation continuation) {
        PlayerAdapter player = new VelocityPlayerAdapter(event.getPlayer());

        Authentication auth = prepareAuthentication(player, AuthType.REGISTER);
        plugin.sendAuthenticationData(auth);

        continuation.resume();
    }

    @Subscribe
    public void onWrongPassword(WrongPasswordEvent event, Continuation continuation) {
        PlayerAdapter player = new VelocityPlayerAdapter(event.getPlayer());

        Authentication auth = prepareAuthentication(player, AuthType.WRONG);
        plugin.sendAuthenticationData(auth);

        continuation.resume();
    }

}