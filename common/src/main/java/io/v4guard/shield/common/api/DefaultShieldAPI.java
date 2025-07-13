package io.v4guard.shield.common.api;

import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.common.universal.UniversalPlugin;
import org.jetbrains.annotations.Nullable;

public class DefaultShieldAPI implements ShieldAPI {

    private final UniversalPlugin plugin;

    public DefaultShieldAPI(UniversalPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public AuthenticationHook getAuthHook() {
        return plugin.getActiveAuthenticationHook();
    }

    @Override
    public void registerAuthHook(AuthenticationHook hook) {
        plugin.registerAuthHook(hook);
    }

    @Override
    public void unregisterAuthHook(AuthenticationHook hook) {
        plugin.unregisterAuthHook(hook);
    }

    @Override
    public void sendAuthenticationData(Authentication auth) {
        plugin.sendAuthenticationData(auth);
    }

    @Override
    public ShieldPlatform getPlatform() {
        return plugin.getCommon().getShieldPlatform();
    }

    @Override
    public @Nullable ConnectedCounterService getConnectedCounterService() {
        return plugin.getConnectedPlayers();
    }

    @Override
    public void setConnectedCounterService(ConnectedCounterService connectedCounterService) {
        plugin.setRegisteredConnectedCounterService(connectedCounterService);
    }

}
