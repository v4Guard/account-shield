package io.v4guard.shield.bungee.messenger;

import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.constants.ShieldConstants;
import io.v4guard.shield.common.messenger.PluginMessenger;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeePluginMessageProcessor extends PluginMessenger implements Listener {

    public BungeePluginMessageProcessor(ShieldCommon shieldCommon) {
        super(shieldCommon);
    }


    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSender() instanceof Server)) {
            return;
        }

        if (!event.getTag().equals(ShieldConstants.LEGACY_PLUGIN_MESSAGE_CHANNEL) &&
                    !event.getTag().equals(ShieldConstants.MODERN_PLUGIN_MESSAGE_CHANNEL)
        ) return;

        super.parsePluginMessage(event.getData());
    }

}
