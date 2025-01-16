package io.v4guard.shield.velocity.messenger;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.messenger.PluginMessenger;
import io.v4guard.shield.velocity.ShieldVelocity;

public class VelocityPluginMessageProcessor extends PluginMessenger {

    public VelocityPluginMessageProcessor(ShieldCommon shieldCommon) {
        super(shieldCommon);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event, Continuation continuation) {
        if (!(event.getSource() instanceof ServerConnection) || !ShieldVelocity.IDENTIFIERS.contains(event.getIdentifier())) {
            continuation.resume();
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        super.parsePluginMessage(event.getData());
        continuation.resume();
    }
}
