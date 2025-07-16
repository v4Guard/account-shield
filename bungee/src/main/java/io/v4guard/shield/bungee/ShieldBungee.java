package io.v4guard.shield.bungee;

import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.bungee.hooks.JPremiumBungeeHook;
import io.v4guard.shield.bungee.hooks.nLoginBungeeHook;
import io.v4guard.shield.bungee.listener.BungeeRedisBungeeListener;
import io.v4guard.shield.bungee.listener.AccountLimitJoinListener;
import io.v4guard.shield.bungee.listener.PremiumCheckListener;
import io.v4guard.shield.bungee.messenger.BungeePluginMessageProcessor;
import io.v4guard.shield.bungee.service.BungeeConnectedCountService;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.api.DefaultShieldAPI;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.constants.ShieldConstants;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.common.listener.DiscoverListener;
import io.v4guard.shield.common.messenger.PluginMessenger;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.common.universal.UniversalPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShieldBungee extends Plugin implements UniversalPlugin {

    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private ConnectedCounterService connectedCounterService;
    private PluginMessenger messenger;
    private final Logger logger = getLogger();

    @Override
    public void onEnable() {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldPlatform.BUNGEE);

            ShieldAPI shieldAPI = new DefaultShieldAPI(this);
            shieldCommon.registerShieldAPI(shieldAPI);
            shieldCommon.getConnectorAPI()
                    .getEventRegistry()
                    .registerListener(new DiscoverListener("accshield:discover", shieldCommon));


            if (shieldCommon.getShieldAPI().getConnectedCounterService() == null) {
                //logger.log(Level.WARNING, "(BungeeCord) Registering default connected counter service");

                boolean registered = false;

                if (this.isPluginEnabled("RedisBungee")) {
                    try {
                        Class.forName("com.imaginarycode.minecraft.redisbungee.api.events.IPubSubMessageEvent");
                        logger.log(Level.INFO, "(BungeeCord) Detected RedisBungee, hooking into it");
                        RedisBungeeConnectedCounterService redisBungeeConnectedCounterService = new RedisBungeeConnectedCounterService(this.getCommon());
                        this.connectedCounterService = redisBungeeConnectedCounterService;
                        this.getProxy().getPluginManager().registerListener(this, new BungeeRedisBungeeListener(this, redisBungeeConnectedCounterService));
                        registered = true;
                    } catch (Exception e) {
                        logger.log(Level.SEVERE,"Your RedisBungee version is outdated. Please update to the latest version called ValioBungee or make your own implementation.");
                    }
                }

                if (!registered) {
                    logger.log(Level.WARNING, "(BungeeCord) No connected counter service found, using default implementation");
                    this.connectedCounterService = new BungeeConnectedCountService(this);
                }

                this.getServer().getPluginManager().registerListener(this, new AccountLimitJoinListener(this,connectedCounterService));
            }

            this.checkForHooks();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "(BungeeCord) Failed to enable", e);
            return;
        }

        BungeePluginMessageProcessor bungeeMessenger = new BungeePluginMessageProcessor(shieldCommon);
        this.messenger = bungeeMessenger;
        getProxy().getPluginManager().registerListener(this, bungeeMessenger);

        getServer().registerChannel(ShieldConstants.LEGACY_PLUGIN_MESSAGE_CHANNEL);
        getServer().registerChannel(ShieldConstants.MODERN_PLUGIN_MESSAGE_CHANNEL);

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
            this.getProxy().getPluginManager().registerListener(this, (Listener) hook);
        }
    }

    @Override
    public void unregisterAuthHook(AuthenticationHook hook) {
        if (this.activeHook == null) {
            throw new IllegalStateException("No authentication hook is currently registered");
        }

        if (this.activeHook.getHookName().equalsIgnoreCase(hook.getHookName())) {
            throw new IllegalArgumentException("The hook is not registered");
        }

        this.logger.info("Unregistered authentication hook: " + hook.getHookName());

        if (this.activeHook instanceof Listener) {
            this.getProxy().getPluginManager().unregisterListener((Listener) this.activeHook);
        }
        this.activeHook = null;
    }

    @Override
    public void sendAuthenticationData(Authentication auth) {
        this.shieldCommon.sendMessage(auth);
    }

    @Override
    public ConnectedCounterService getConnectedPlayers() {
        return this.connectedCounterService;
    }

    @Override
    public void setRegisteredConnectedCounterService(ConnectedCounterService connectedCounterService) {
        this.connectedCounterService = connectedCounterService;
    }

    @Override
    public PluginMessenger getMessenger() {
        return messenger;
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return this.getProxy().getPluginManager().getPlugin(pluginName) != null;
    }

    private void checkForHooks() {
        if (this.isPluginEnabled("nLogin")) {
            this.registerAuthHook(new nLoginBungeeHook(this));
        }

        if (this.isPluginEnabled("JPremium")) {
            this.registerAuthHook(new JPremiumBungeeHook(this));
        }

        if (this.activeHook == null) {
            logger.log(Level.SEVERE, "(BungeeCord) No authentication hooks found.");
            logger.log(Level.SEVERE, "(BungeeCord) Register your own hook or install one of these authentication plugins to use account shield:");
            logger.log(Level.SEVERE, "(BungeeCord) Available hooks: nLogin, JPremium");

            this.getServer().getPluginManager().registerListener(this, new PremiumCheckListener(this));
        }
    }
}