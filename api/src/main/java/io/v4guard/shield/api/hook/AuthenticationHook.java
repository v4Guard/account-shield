package io.v4guard.shield.api.hook;

import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.adapter.PlayerAdapter;
import io.v4guard.shield.api.auth.AuthType;
import io.v4guard.shield.api.auth.Authentication;


/**
 * The base class for authentication hooks
 * <p>
 * An authentication hook is a class handles the authentication of a player.
 * Usually, this class would have the corresponding listeners for the authentication events fired by the authentication plugin.
 * <p>
 * You need a {@link PlayerAdapter} to prepare the authentication user and them pass it to the {@link #prepareAuthentication(PlayerAdapter, AuthType)} method
 * to create the authentication data.
 * <p>
 * Finally, you need to send the data to the connector by calling the {@link ShieldAPI#sendAuthenticationData(Authentication)} method
 *
 * @see PlayerAdapter
 * @see AuthType
 * @see Authentication
 */
public abstract class AuthenticationHook {

    private final String hookName;

    /**
     * Create a new authentication hook
     * @param hookName the name of the authentication hook
     */
    public AuthenticationHook(String hookName) {
        this.hookName = hookName;
    }

    public String getHookName() {
        return hookName;
    }

    protected Authentication prepareAuthentication(
            PlayerAdapter player, AuthType authType
    ) {
        return new Authentication(
                player.getName(),
                player.getUniqueId(),
                authType,
                player.hasPermission("v4guard.accshield")
        );
    }
}
