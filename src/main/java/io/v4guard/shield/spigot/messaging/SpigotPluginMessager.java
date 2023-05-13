package io.v4guard.shield.spigot.messaging;

import io.v4guard.plugin.core.v4GuardCore;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.auth.ConnectorAuthentication;
import io.v4guard.shield.core.messaging.PluginMessager;
import io.v4guard.shield.spigot.v4GuardShieldSpigot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SpigotPluginMessager extends PluginMessager {

    private boolean pluginConnectorFound;

    public SpigotPluginMessager(JavaPlugin plugin) {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, PluginMessager.CHANNEL);
        pluginConnectorFound = Bukkit.getPluginManager().isPluginEnabled("v4guard-plugin");
    }

    @Override
    public void sendMessage(Authentication auth) {

        if(pluginConnectorFound){
            ConnectorAuthentication connectorAuth = new ConnectorAuthentication(auth);
            ConnectorAuthentication.sendMessage(connectorAuth);
            return;
        }

        Player player = Bukkit.getPlayer(auth.getUsername());

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {

            out.writeUTF(auth.serialize().toJson());
            player.sendPluginMessage(v4GuardShieldSpigot.getInstance(),
                    PluginMessager.CHANNEL,
                    b.toByteArray()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
