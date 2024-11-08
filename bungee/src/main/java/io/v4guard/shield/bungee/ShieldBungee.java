package io.v4guard.shield.bungee;

import io.v4guard.connector.common.UnifiedLogger;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.bungee.hooks.JPremiumBungeeHook;
import io.v4guard.shield.bungee.hooks.nLoginBungeeHook;
import io.v4guard.shield.bungee.listener.BungeeRedisBungeeListener;
import io.v4guard.shield.bungee.service.BungeeConnectedCountService;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.common.mode.ShieldMode;
import io.v4guard.shield.common.universal.UniversalPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShieldBungee extends Plugin implements UniversalPlugin {
    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private final Logger logger = UnifiedLogger.get();


    @Override
    public void onEnable() {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldMode.BUNGEE);

            if (shieldCommon.getShieldAPI().getConnectedCounterService() == null) {
                logger.log(Level.WARNING, "(BungeeCord) Registering default connected counter service");

                if (this.isPluginEnabled("RedisBungee")) {
                    logger.log(Level.INFO, "(BungeeCord) Detected RedisBungee, hooking into it");
                    RedisBungeeConnectedCounterService redisBungeeConnectedCounterService = new RedisBungeeConnectedCounterService();
                    shieldCommon.getShieldAPI().setConnectedCounterService(redisBungeeConnectedCounterService);
                    this.getProxy().getPluginManager().registerListener(this, new BungeeRedisBungeeListener(redisBungeeConnectedCounterService));
                } else {
                    shieldCommon.getShieldAPI().setConnectedCounterService(new BungeeConnectedCountService(this));
                }
            }

            this.checkForHooks();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "(BungeeCord) Failed to enable", e);
            return;
        }

        logger.info("Enabling... [DONE]");
    }

    @Override
    public void onDisable() {
        logger.info("Disabling...");
        v4GuardShieldProvider.unregister();
    }

    public ProxyServer getServer() {
        return this.getProxy();
    }

    @Override
    public ShieldCommon getCommon() {
        return shieldCommon;
    }

    @Override
    public ConnectedCounterService getConnectedPlayers() {
        return shieldCommon.getShieldAPI().getConnectedCounterService();
    }

    @Override
    public AuthenticationHook getActiveAuthenticationHook() {
        return activeHook;
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return this.getProxy().getPluginManager().getPlugin(pluginName) != null;
    }

    private void checkForHooks() {
        if (this.isPluginEnabled("nLogin")) {
            this.activeHook = new nLoginBungeeHook(this);
        }

        if (this.isPluginEnabled("JPremium")) {
            this.activeHook = new JPremiumBungeeHook(this);
        }

        if (this.activeHook == null) {
            logger.log(Level.SEVERE, "(BungeeCord) No authentication hooks found.");
            logger.log(Level.SEVERE, "(BungeeCord) Register your own hook or install one of these authentication plugins to use account shield:");
            logger.log(Level.SEVERE, "(BungeeCord) Available hooks: nLogin, JPremium");
        } else {
            logger.info("Hooked into " + this.activeHook.getHookName());
            this.getProxy().getPluginManager().registerListener(this, (Listener) this.activeHook);

        }
    }
}