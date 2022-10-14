package io.v4guard.shield.bungee;

import io.v4guard.shield.bungee.hooks.JPremiumBungeeHook;
import io.v4guard.shield.bungee.hooks.nLoginBungeeHook;
import io.v4guard.shield.bungee.messaging.BungeePluginMessager;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.mode.ShieldMode;
import io.v4guard.shield.core.v4GuardShieldCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class v4GuardShieldBungee extends Plugin {

    private static v4GuardShieldCore core;
    private static v4GuardShieldBungee v4GuardShieldBungee;
    private AuthenticationHook authHook;

    @Override
    public void onEnable(){
        this.getProxy().getConsole().sendMessage(new TextComponent("§e[v4guard-account-shield] (Bungee) Enabling..."));
        try {
            core = new v4GuardShieldCore(ShieldMode.BUNGEE);
            core.setMessager(new BungeePluginMessager());
            checkForHooks();
        } catch (Exception e) {
            this.getProxy().getConsole().sendMessage(new TextComponent("§c[v4guard-account-shield] (Bungee) Enabling... [ERROR]"));
            this.getProxy().getConsole().sendMessage(new TextComponent("§cPlease check the console for more information and report this error."));
            e.printStackTrace();
            return;
        }
        v4GuardShieldBungee = this;
        this.getProxy().getConsole().sendMessage(new TextComponent("§e[v4guard-account-shield] (Bungee) Enabling... [DONE]"));
    }

    public static v4GuardShieldCore getShieldInstance() {
        return core;
    }

    public static v4GuardShieldBungee getInstance() {
        return v4GuardShieldBungee;
    }

    public AuthenticationHook getAuthHook() {
        return authHook;
    }

    private void checkForHooks(){
        if(this.getProxy().getPluginManager().getPlugin("nLogin") != null){
            this.authHook = new nLoginBungeeHook(this);
        }
        if(this.getProxy().getPluginManager().getPlugin("JPremium") != null){
            this.authHook = new JPremiumBungeeHook(this);
        }
        if(authHook == null) {
            CommandSender consoleSender = this.getProxy().getConsole();
            consoleSender.sendMessage(new TextComponent("§c[v4guard-account-shield] (Bungee) No authentication hooks found."));
            consoleSender.sendMessage(new TextComponent("§c[v4guard-account-shield] (Bungee) Install one of these authentication plugins to use account shield:"));
            consoleSender.sendMessage(new TextComponent("§cAvailable hooks: nLogin, JPremium"));
        }
    }
}
