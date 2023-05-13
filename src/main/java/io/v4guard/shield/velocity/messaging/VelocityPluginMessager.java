package io.v4guard.shield.velocity.messaging;

import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.auth.ConnectorAuthentication;
import io.v4guard.shield.core.messaging.PluginMessager;

public class VelocityPluginMessager extends PluginMessager {

    @Override
    public void sendMessage(Authentication auth) {
        ConnectorAuthentication connectorAuth = new ConnectorAuthentication(auth);
        ConnectorAuthentication.sendMessage(connectorAuth);
    }
}
