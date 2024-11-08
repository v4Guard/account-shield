package io.v4guard.shield.spigot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.v4guard.connector.common.UnifiedLogger;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.connector.common.constants.ShieldChannels;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.hook.AuthenticationHook;
import io.v4guard.shield.common.mode.ShieldMode;
import io.v4guard.shield.common.universal.UniversalPlugin;
import io.v4guard.shield.spigot.hooks.AuthMeSpigotHook;
import io.v4guard.shield.spigot.hooks.nLoginSpigotHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShieldSpigot extends JavaPlugin  implements UniversalPlugin {

    private AuthenticationHook activeHook;
    private ShieldCommon shieldCommon;
    private ObjectMapper objectMapper;
    private final Logger logger = Logger.getLogger("v4guard-account-shield");

    @Override
    public void onEnable() {
        logger.info("Enabling...");

        try {
            shieldCommon = new ShieldCommon(ShieldMode.SPIGOT);

            this.objectMapper = new ObjectMapper();

            this.checkForHooks();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "(Velocity) Enabling... [ERROR]", e);
            return;
        }

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
        return shieldCommon.getShieldAPI().getConnectedCounterService();
    }

    @Override
    public AuthenticationHook getActiveAuthenticationHook() {
        return activeHook;
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
            logger.info("(Spigot) Hooked into " + this.activeHook.getHookName());
            this.getServer().getPluginManager().registerEvents((Listener) this.activeHook, this);
        }
    }


    public void sendPluginMessage(Authentication auth) {
        Player player = Bukkit.getPlayer(auth.getUsername());
        if (player == null) return;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(objectMapper.convertValue(auth, Authentication.class).toString());
            player.sendPluginMessage(this,
                    ShieldChannels.BUNGEE_CHANNEL,
                    b.toByteArray()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
