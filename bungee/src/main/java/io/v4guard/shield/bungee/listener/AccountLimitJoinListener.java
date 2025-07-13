package io.v4guard.shield.bungee.listener;

import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.bungee.ShieldBungee;
import io.v4guard.shield.common.util.StringUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

public class AccountLimitJoinListener implements Listener {

    private final ShieldBungee plugin;
    private final ConnectedCounterService connectedCounterService;
    // Avoids creating a new list every time - mem optimization
    private final  List<String> defaultMessage = Collections.singletonList("§d▲ §lV4GUARD §7Too many accounts connected from this IP :(.");

    public AccountLimitJoinListener(ShieldBungee plugin, ConnectedCounterService connectedCounterService) {
        this.plugin = plugin;
        this.connectedCounterService = connectedCounterService;
    }

    @EventHandler
    public void onPlayerJoin(LoginEvent event) {
        InetAddress address = ((InetSocketAddress) event.getConnection().getSocketAddress()).getAddress();

        int connectedAccounts = connectedCounterService.getConnectedAccounts(address);
        int maxAccounts = Integer.parseInt(plugin.getCommon().getAddon().getSettings().get("accountLimit"));

        if (maxAccounts == -1) return;

        if (connectedAccounts >= maxAccounts) {
            List<String> message = plugin.getCommon().getAddon().getMessages().getOrDefault("tooManyAccounts", defaultMessage);
            event.setCancelled(true);
            event.setReason(new ComponentBuilder(StringUtils.buildMultilineString(message)).create()[0]);
        }
    }

}
