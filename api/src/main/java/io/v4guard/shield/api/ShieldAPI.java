package io.v4guard.shield.api;

import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.api.service.ConnectedCounterService;
import org.jetbrains.annotations.Nullable;

public interface ShieldAPI {

    /**
     * Get the platform that the shield is currently running on
     * @return the platform
     */
    ShieldPlatform getPlatform();

    /**
     * Get registered authentication hook
     * @return the authentication hook or null if no authentication hook is registered
     */
    @Nullable AuthenticationHook getAuthHook();

    /**
     * Get the connected counter service that is currently used by the shield
     * @return the connected counter service
     */
    ConnectedCounterService getConnectedCounterService();

    /**
     * Register an authentication hook for a specific auth plugin
     * @param hook the authentication hook to register
     * @throws IllegalArgumentException if the hook is already registered, or it is not supported
     */
    void registerAuthHook(AuthenticationHook hook);

    /**
     * Unregister an authentication hook
     * @param hook the authentication hook to unregister
     * @throws IllegalArgumentException if the hook is not registered, or it is not supported
     */
    void unregisterAuthHook(AuthenticationHook hook);

    /**
     * Send authentication data to the connector
     * @param auth the authentication data
     */
    void sendAuthenticationData(Authentication auth);

    /**
     * Set the connected counter service implementation that will be used by the shield
     * @param connectedCounterService the connected counter service
     */
    void setConnectedCounterService(ConnectedCounterService connectedCounterService);

}
