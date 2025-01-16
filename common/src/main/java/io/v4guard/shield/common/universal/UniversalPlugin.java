package io.v4guard.shield.common.universal;

import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.common.messenger.PluginMessenger;

public interface UniversalPlugin {

    ShieldCommon getCommon();
    ConnectedCounterService getConnectedPlayers();
    AuthenticationHook getActiveAuthenticationHook();
    PluginMessenger getMessenger();
    boolean isPluginEnabled(String pluginName);
}
