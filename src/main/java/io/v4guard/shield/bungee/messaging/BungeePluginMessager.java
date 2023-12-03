package io.v4guard.shield.bungee.messaging;

import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.auth.ConnectorAuthentication;
import io.v4guard.shield.core.messaging.PluginMessager;

public class BungeePluginMessager extends PluginMessager {

    @Override
    public void sendMessage(Authentication auth) {
        ConnectorAuthentication connectorAuth = new ConnectorAuthentication(auth);
        ConnectorAuthentication.sendMessage(connectorAuth);
    }
}
