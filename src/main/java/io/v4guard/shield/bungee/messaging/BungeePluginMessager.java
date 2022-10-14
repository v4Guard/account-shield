package io.v4guard.shield.bungee.messaging;

import io.v4guard.shield.bungee.v4GuardShieldBungee;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.auth.ConnectorAuthentication;
import io.v4guard.shield.core.messaging.PluginMessager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeePluginMessager extends PluginMessager {

    @Override
    public void sendMessage(Authentication auth) {
        ConnectorAuthentication connectorAuth = new ConnectorAuthentication(auth.getUsername(), auth.getAuthType());
        ConnectorAuthentication.sendMessage(connectorAuth);
    }
}
