package io.v4guard.shield.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.common.util.StringUtils;
import io.v4guard.shield.velocity.ShieldVelocity;
import net.kyori.adventure.text.Component;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

public class AccountLimitJoinListener {

    private final ShieldVelocity plugin;
    private final ConnectedCounterService connectedCounterService;
    private final List<String> defaultMessage = Collections.singletonList("§d▲ §lV4GUARD §7Too many accounts connected from this IP :(.");

    public AccountLimitJoinListener(ShieldVelocity plugin, ConnectedCounterService connectedCounterService) {
        this.plugin = plugin;
        this.connectedCounterService = connectedCounterService;
    }

    @Subscribe
    public void onPlayerJoin(LoginEvent event) {
        InetAddress address = event.getPlayer().getRemoteAddress().getAddress();

        int connectedAccounts = connectedCounterService.getConnectedAccounts(address);
        int maxAccounts = Integer.parseInt(plugin.getCommon().getAddon().getSettings().get("accountLimit"));

        if (maxAccounts == -1) return;

        if (connectedAccounts >= maxAccounts) {
            List<String> message = plugin.getCommon()
                    .getAddon().getMessages()
                    .getOrDefault("tooManyAccounts", defaultMessage);

            event.setResult(LoginEvent.ComponentResult.denied(
                    Component.text(StringUtils.buildMultilineString(message))
            ));
        }
    }
}
