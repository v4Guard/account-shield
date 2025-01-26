package io.v4guard.shield.common.universal;

import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.common.messenger.PluginMessenger;

public interface UniversalPlugin {

    ShieldCommon getCommon();
    PluginMessenger getMessenger();
    boolean isPluginEnabled(String pluginName);

    ConnectedCounterService getConnectedPlayers();
    void setRegisteredConnectedCounterService(ConnectedCounterService connectedCounterService);

    AuthenticationHook getActiveAuthenticationHook();
    void registerAuthHook(AuthenticationHook hook);
    void unregisterAuthHook(AuthenticationHook hook);

    void sendAuthenticationData(Authentication auth);
}
