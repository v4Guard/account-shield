package io.v4guard.shield.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.v4guard.plugin.core.compatibility.Messenger;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.mode.ShieldMode;
import io.v4guard.shield.core.v4GuardShieldCore;
import io.v4guard.shield.velocity.hooks.nLoginVelocityHook;
import io.v4guard.shield.velocity.messaging.VelocityPluginMessager;
import net.kyori.adventure.text.Component;

import java.util.logging.Logger;

@Plugin(
        id = "v4guard-account-shield",
        name = "v4Guard Account Shield",
        version = v4GuardShieldCore.pluginVersion,
        url = "https://v4guard.io",
        description = "v4Guard Account Shield for Minecraft Servers",
        authors = {"v4Guard"},
        dependencies = {
                @Dependency(id = "v4guard-plugin"),
                @Dependency(id = "nLogin", optional = true)
        }
)
public class v4GuardShieldVelocity {

    private static v4GuardShieldCore core;
    private static v4GuardShieldVelocity v4GuardShieldVelocity;
    private AuthenticationHook authHook;

    private ProxyServer server;
    private Logger logger;
    private Messenger messenger;

    @Inject
    public v4GuardShieldVelocity(ProxyServer server, Logger logger) {
        v4GuardShieldVelocity = this;
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        sendConsoleMessage("§e[v4guard-account-shield] (Velocity) Enabling...");
        try {
            core = new v4GuardShieldCore(ShieldMode.VELOCITY);
            core.setMessager(new VelocityPluginMessager());
            checkForHooks();
        } catch (Exception e) {
            sendConsoleMessage("§c[v4guard-account-shield] (Velocity) Enabling... [ERROR]");
            sendConsoleMessage("§cPlease check the console for more information and report this error.");
            e.printStackTrace();
            return;
        }
        v4GuardShieldVelocity = this;
        sendConsoleMessage("§e[v4guard-account-shield] (Velocity) Enabling... [DONE]");
    }

    public static v4GuardShieldCore getShieldInstance() {
        return core;
    }

    public static v4GuardShieldVelocity getInstance() {
        return v4GuardShieldVelocity;
    }

    public AuthenticationHook getAuthHook() {
        return authHook;
    }

    public void registerHook(AuthenticationHook authHook) {
        if(this.authHook != null){
            sendConsoleMessage("§c[v4guard-account-shield] (Velocity) " + authHook.getHookName() + " hook unregistered");
        }
        this.authHook = authHook;
    }

    public ProxyServer getServer() {
        return server;
    }

    private void checkForHooks(){
        if (this.getServer().getPluginManager().getPlugin("nlogin").isPresent()){
            this.authHook = new nLoginVelocityHook(this);
        }
        if (authHook == null) {
            sendConsoleMessage("§c[v4guard-account-shield] (Velocity) No authentication hooks found.");
            sendConsoleMessage("§c[v4guard-account-shield] (Velocity) Register your own hook or install one of these authentication plugins to use account shield:");
            sendConsoleMessage("§cAvailable hooks: nLogin");
        }
    }
    
    public void sendConsoleMessage(String message) {
        this.getServer().getConsoleCommandSource().sendMessage(Component.text(message));
    }
}
