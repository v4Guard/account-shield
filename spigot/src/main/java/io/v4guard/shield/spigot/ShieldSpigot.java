package io.v4guard.shield.spigot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.api.DefaultShieldAPI;
import io.v4guard.shield.common.constants.ShieldConstants;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.common.messenger.PluginMessenger;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.common.universal.UniversalPlugin;
import io.v4guard.shield.spigot.hooks.AuthMeSpigotHook;
import io.v4guard.shield.spigot.hooks.nLoginSpigotHook;
import io.v4guard.shield.api.auth.Authentication;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShieldSpigot extends JavaPlugin implements UniversalPlugin {

    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private final Logger logger = Logger.getLogger("v4guard-account-shield");

    @Override
    public void onEnable() {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldPlatform.SPIGOT);

            ShieldAPI shieldAPI = new DefaultShieldAPI(this);
            Bukkit.getServer().getServicesManager().register(
                    ShieldAPI.class,
                    shieldAPI,
                    this,
                    ServicePriority.Normal
            );

            shieldCommon.registerShieldAPI(shieldAPI);
            this.checkForHooks();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "(Velocity) Enabling... [ERROR]", e);
            return;
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, ShieldConstants.MODERN_PLUGIN_MESSAGE_CHANNEL);

        logger.info("Enabling... [DONE]");
    }

    @Override
    public void onDisable() {
        logger.info("Disabling...");
        v4GuardShieldProvider.unregister();
    }

    @Override
    public ShieldCommon getCommon() {
        return shieldCommon;
    }

    @Override
    public ConnectedCounterService getConnectedPlayers() {
        throw new UnsupportedOperationException("Spigot does not support getting the connected counter service");
    }

    @Override
    public void setRegisteredConnectedCounterService(ConnectedCounterService connectedCounterService) {
        throw new UnsupportedOperationException("Spigot does not support setting the connected counter service");
    }

    @Override
    public AuthenticationHook getActiveAuthenticationHook() {
        return activeHook;
    }

    @Override
    public void registerAuthHook(AuthenticationHook hook) {
        if (this.activeHook != null) {
            throw new IllegalStateException("An authentication hook is already registered");
        }

        this.logger.info("Registered authentication hook: " + hook.getHookName());
        this.activeHook = hook;
        if (hook instanceof Listener) {
            this.getServer().getPluginManager().registerEvents((Listener) hook, this);
        }
    }

    @Override
    public void unregisterAuthHook(AuthenticationHook hook) {
        throw new UnsupportedOperationException("Spigot does not support unregistering authentication hooks");
    }

    @Override
    public void sendAuthenticationData(Authentication auth) {
        Player player = Bukkit.getPlayerExact(auth.getUsername());
        if (player == null) return;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        try {
            out.writeUTF(shieldCommon.getObjectMapper().writeValueAsString(auth));
            player.sendPluginMessage(this,
                    ShieldConstants.MODERN_PLUGIN_MESSAGE_CHANNEL,
                    out.toByteArray()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public PluginMessenger getMessenger() {
        throw new UnsupportedOperationException("Spigot cannot parse plugin messages");
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return this.getServer().getPluginManager().isPluginEnabled(pluginName);
    }

    private void checkForHooks() {
        if (this.isPluginEnabled("authme")) {
            this.activeHook = new AuthMeSpigotHook(this);
        }

        if (this.isPluginEnabled("nlogin")) {
            this.activeHook = new nLoginSpigotHook(this);
        }

        if (this.activeHook == null) {
            logger.log(Level.SEVERE, "(Spigot) No authentication hooks found.");
            logger.log(Level.SEVERE, "(Spigot) Register your own hook or install one of these authentication plugins to use account shield:");
            logger.log(Level.SEVERE, "(Spigot) Available hooks: nLogin, Authme");
        } else {
            this.registerAuthHook(this.activeHook);
        }
    }

}