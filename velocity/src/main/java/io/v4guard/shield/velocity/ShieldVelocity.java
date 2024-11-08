package io.v4guard.shield.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.v4guard.connector.common.UnifiedLogger;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.api.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.common.mode.ShieldMode;
import io.v4guard.shield.common.universal.UniversalPlugin;
import io.v4guard.shield.velocity.hooks.JPremiumVelocityHook;
import io.v4guard.shield.velocity.hooks.nLoginVelocityHook;
import io.v4guard.shield.velocity.listener.VelocityRedisBungeeListener;
import io.v4guard.shield.velocity.service.VelocityConnectedCounterService;

import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(
        id = "v4guard-account-shield",
        name = "v4Guard Account Shield",
        version = "2.5.0",
        url = "https://v4guard.io",
        description = "v4Guard Account Shield for Minecraft Servers",
        authors = {"v4Guard"},
        dependencies = {
                @Dependency(id = "v4guard-plugin"),
                @Dependency(id = "nLogin", optional = true),
                @Dependency(id = "jpremium", optional = true),
                @Dependency(id = "redisbnugee", optional = true)
        }
)
public class ShieldVelocity implements UniversalPlugin {

    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private final ProxyServer proxyServer;
    private final Logger logger = UnifiedLogger.get();

    @Inject
    public ShieldVelocity(
            ProxyServer proxyServer
    ) {
        this.proxyServer = proxyServer;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldMode.VELOCITY);

            if (shieldCommon.getShieldAPI().getConnectedCounterService() == null) {
                logger.log(Level.SEVERE, "(Velocity) Enabling... [INFO] Registering default connected counter service");

                if (this.isPluginEnabled("redisbungee")) {
                    logger.log(Level.SEVERE, "(Velocity) Detected RedisBungee, hooking into it");
                    RedisBungeeConnectedCounterService redisBungeeConnectedCounterService = new RedisBungeeConnectedCounterService();
                    shieldCommon.getShieldAPI().setConnectedCounterService(redisBungeeConnectedCounterService);
                    this.getServer().getEventManager().register(this, new VelocityRedisBungeeListener(redisBungeeConnectedCounterService));
                } else {
                    logger.log(Level.SEVERE, "(Velocity) Registering default connected counter service");
                    shieldCommon.getShieldAPI().setConnectedCounterService(new VelocityConnectedCounterService(proxyServer));
                }
            }

            this.checkForHooks();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "(Velocity) Enabling... [ERROR]", e);
            return;
        }

        logger.info("Enabling... [DONE]");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Disabling...");
        v4GuardShieldProvider.unregister();
    }

    public ProxyServer getServer() {
        return proxyServer;
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
        return this.proxyServer.getPluginManager().isLoaded(pluginName);
    }

    private void checkForHooks() {
        if (this.isPluginEnabled("nlogin")) {
            this.activeHook = new nLoginVelocityHook(this);
        }

        if (this.isPluginEnabled("jpremium")) {
            this.activeHook = new JPremiumVelocityHook(this);
        }

        if (this.activeHook == null) {
            logger.log(Level.SEVERE, "(Velocity) No authentication hooks found.");
            logger.log(Level.SEVERE, "(Velocity) Register your own hook or install one of these authentication plugins to use account shield:");
            logger.log(Level.SEVERE, "(Velocity) Available hooks: nLogin, JPremium");
        } else {
            logger.info("Hooked into " + this.activeHook.getHookName());
            this.proxyServer.getEventManager().register(this, this.activeHook);
        }
    }
}
