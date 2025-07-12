package io.v4guard.shield.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.v4guard.connector.api.ConnectorAPI;
import io.v4guard.connector.api.socket.Addon;
import io.v4guard.connector.api.v4GuardConnectorProvider;
import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.api.platform.ShieldPlatform;
import io.v4guard.shield.common.listener.DiscoverListener;

public class ShieldCommon {

    private ShieldAPI shieldAPI;
    private ConnectorAPI connectorAPI;
    private final ShieldPlatform shieldPlatform;
    private final ObjectMapper objectMapper;

    public ShieldCommon(ShieldPlatform platform) {
        this.shieldPlatform = platform;
        this.objectMapper = new ObjectMapper();
        if (shieldPlatform != ShieldPlatform.SPIGOT) {
            this.connectorAPI = v4GuardConnectorProvider.get();
            connectorAPI.getEventRegistry().registerListener(new DiscoverListener("accshield:discover", this));
        }
    }

    public void registerShieldAPI(ShieldAPI shieldAPI) {
        this.shieldAPI = shieldAPI;
        v4GuardShieldProvider.register(shieldAPI);
    }

    public ShieldPlatform getShieldPlatform() {
        return shieldPlatform;
    }

    public ShieldAPI getShieldAPI() {
        return shieldAPI;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void sendMessage(Authentication auth) {
        if (!connectorAPI.getConnection().isReady()) return;
        if (!connectorAPI.getActiveSettings().getActiveAddons().get("accshield").isEnabled()) return;

        connectorAPI.getConnection().send(
                "accshield:login",
                objectMapper.convertValue(auth, ObjectNode.class).toString()
        );
    }

    public void sendMessage(String channel, String payload) {
        if (!connectorAPI.getConnection().isReady()) return;
        if (!connectorAPI.getActiveSettings().getActiveAddons().get("accshield").isEnabled()) return;

        connectorAPI.getConnection().send(channel, payload);
    }

    public Addon getAddon() {
        return connectorAPI.getActiveSettings().getActiveAddons().get("accshield");
    }
}
