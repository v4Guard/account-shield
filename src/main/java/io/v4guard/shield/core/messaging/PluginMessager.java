package io.v4guard.shield.core.messaging;

import io.v4guard.shield.core.auth.Authentication;

public abstract class PluginMessager {

//    This is a bit of a "dirty" solution.
//    If we place a custom channel name the message does not reach BungeeCord/Velocity.
//    We will temporarily use the BungeeCord channel until we find a solution to the problem.
//    protected static final String CHANNEL = "v4guard:accountshield";

    protected static final String CHANNEL = "BungeeCord";

    public abstract void sendMessage(Authentication authentication);

}
