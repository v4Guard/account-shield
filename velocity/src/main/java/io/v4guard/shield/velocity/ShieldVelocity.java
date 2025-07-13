package io.v4guard.shield.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.api.DefaultShieldAPI;
import io.v4guard.shield.common.api.service.RedisBungeeConnectedCounterService;
import io.v4guard.shield.common.constants.ShieldConstants;
import io.v4guard.shield.api.hook.AuthenticationHook;
import io.v4guard.shield.common.messenger.PluginMessenger;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.common.universal.UniversalPlugin;
import io.v4guard.shield.velocity.hooks.JPremiumVelocityHook;
import io.v4guard.shield.velocity.hooks.nLoginVelocityHook;
import io.v4guard.shield.velocity.listener.AccountLimitJoinListener;
import io.v4guard.shield.velocity.listener.PremiumCheckListener;
import io.v4guard.shield.velocity.listener.VelocityRedisBungeeListener;
import io.v4guard.shield.velocity.messenger.VelocityPluginMessageProcessor;
import io.v4guard.shield.velocity.service.VelocityConnectedCounterService;
import org.slf4j.Logger;

import java.util.List;


@Plugin(
        id = "v4guard-account-shield",
        name = "v4Guard Account Shield",
        version = "3.0.0",
        url = "https://v4guard.io",
        description = "v4Guard Account Shield for Minecraft Servers",
        authors = {"v4Guard"},
        dependencies = {
                @Dependency(id = "v4guard-plugin"),
                @Dependency(id = "nlogin", optional = true),
                @Dependency(id = "jpremium", optional = true),
                @Dependency(id = "redisbungee", optional = true)
        }
)
public class ShieldVelocity implements UniversalPlugin {

    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private ConnectedCounterService connectedCounterService;
    private final ProxyServer proxyServer;
    private PluginMessenger messenger;
    private final Logger logger;

    public static final List<ChannelIdentifier> IDENTIFIERS = List.of(
            new LegacyChannelIdentifier(ShieldConstants.LEGACY_PLUGIN_MESSAGE_CHANNEL),
            MinecraftChannelIdentifier.from(ShieldConstants.MODERN_PLUGIN_MESSAGE_CHANNEL)
    );

    @Inject
    public ShieldVelocity(
            ProxyServer proxyServer,
            Logger logger
    ) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldPlatform.VELOCITY);

            ShieldAPI shieldAPI = new DefaultShieldAPI(this);
            shieldCommon.registerShieldAPI(shieldAPI);


            if (shieldCommon.getShieldAPI().getConnectedCounterService() == null) {
                //logger.warn("(Velocity) Registering default connected counter service");

                boolean registered = false;

                if (this.isPluginEnabled("redisbungee")) {
                    logger.info("(Velocity) Detected RedisBungee, hooking into it");
                    RedisBungeeConnectedCounterService redisBungeeConnectedCounterService = new RedisBungeeConnectedCounterService(this.getCommon());

                    this.connectedCounterService = redisBungeeConnectedCounterService;
                    this.getServer().getEventManager().register(this, new VelocityRedisBungeeListener(this, redisBungeeConnectedCounterService));
                    registered = true;
                }

                if (!registered) {
                    logger.warn("(Velocity) No connected counter service found, using default implementation");
                    this.connectedCounterService = new VelocityConnectedCounterService(proxyServer);
                }

                this.getServer().getEventManager().register(this, new AccountLimitJoinListener(this, connectedCounterService));
            }

            this.checkForHooks();
        } catch (Exception e) {
            logger.error("(Velocity) Enabling... [ERROR]", e);
            return;
        }

        IDENTIFIERS.forEach(getServer().getChannelRegistrar()::register);
        messenger = new VelocityPluginMessageProcessor(shieldCommon);

        getServer().getEventManager().register(this, messenger);
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
    public void registerAuthHook(AuthenticationHook hook) {
        if (this.activeHook != null) {
            throw new IllegalStateException("An authentication hook is already registered");
        }

        this.logger.info("Registered authentication hook: {}", hook.getHookName());
        this.activeHook = hook;
        this.proxyServer.getEventManager().register(this, hook);
    }

    @Override
    public AuthenticationHook getActiveAuthenticationHook() {
        return activeHook;
    }

    @Override
    public void unregisterAuthHook(AuthenticationHook hook) {
        if (this.activeHook == null) {
            throw new IllegalStateException("No authentication hook is currently registered");
        }

        if (this.activeHook.getHookName().equalsIgnoreCase(hook.getHookName())) {
            throw new IllegalArgumentException("The hook is not registered");
        }

        this.logger.info("Unregistered authentication hook: {}", hook.getHookName());
        this.proxyServer.getEventManager().unregisterListener(this, hook);
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
        return this.proxyServer.getPluginManager().isLoaded(pluginName);
    }

    public Logger getLogger() {
        return logger;
    }

    private void checkForHooks() {
        if (this.isPluginEnabled("nlogin")) {
            this.registerAuthHook(new nLoginVelocityHook(this));
            return;
        }

        if (this.isPluginEnabled("jpremium")) {
            this.registerAuthHook(new JPremiumVelocityHook(this));
            return;
        }

        if (this.activeHook == null) {
            logger.error("(Velocity) No authentication hooks found.");
            logger.error("(Velocity) Register your own hook or install one of these authentication plugins to use account shield:");
            logger.error("(Velocity) Available hooks: nLogin, JPremium");
            this.proxyServer.getEventManager().register(this, new PremiumCheckListener(this));
        }
    }
}
