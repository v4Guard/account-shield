package io.v4guard.shield.spigot;

import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.mode.ShieldMode;
import io.v4guard.shield.core.v4GuardShieldCore;
import io.v4guard.shield.spigot.hooks.AuthMeSpigotHook;
import io.v4guard.shield.spigot.hooks.nLoginSpigotHook;
import io.v4guard.shield.spigot.messaging.SpigotPluginMessager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class v4GuardShieldSpigot extends JavaPlugin {

    private static v4GuardShieldCore core;
    private static v4GuardShieldSpigot v4GuardSpigot;
    private AuthenticationHook authHook;

    @Override
    public void onEnable(){
        this.getServer().getConsoleSender().sendMessage("§e[v4guard-account-shield] (Spigot) Enabling...");
        try {
            core = new v4GuardShieldCore(ShieldMode.SPIGOT);
            core.setMessager(new SpigotPluginMessager(this));
            checkForHooks();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("§c[v4guard-account-shield] (Spigot) Enabling... [ERROR]");
            this.getServer().getConsoleSender().sendMessage("§cPlease check the console for more information and report this error.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        v4GuardSpigot = this;
        this.getServer().getConsoleSender().sendMessage("§e[v4guard-account-shield] (Spigot) Enabling... [DONE]");
    }

    public static v4GuardShieldCore getShieldInstance() {
        return core;
    }

    public static v4GuardShieldSpigot getInstance() {
        return v4GuardSpigot;
    }

    public AuthenticationHook getAuthHook() {
        return authHook;
    }

    private void checkForHooks(){
        if(Bukkit.getPluginManager().getPlugin("AuthMe") != null){
            this.authHook = new AuthMeSpigotHook(this);
        }
        if(Bukkit.getPluginManager().getPlugin("nLogin") != null){
            this.authHook = new nLoginSpigotHook(this);
        }
        if(authHook == null) {
            ConsoleCommandSender consoleSender = this.getServer().getConsoleSender();
            consoleSender.sendMessage("§c[v4guard-account-shield] (Spigot) No authentication hooks found.");
            consoleSender.sendMessage("§c[v4guard-account-shield] (Spigot) Install one of these authentication plugins to use account shield:");
            consoleSender.sendMessage("§cAvailable hooks: AuthMe, nLogin");
        }
        //TODO add support for JPremium, nLogin
    }
}
