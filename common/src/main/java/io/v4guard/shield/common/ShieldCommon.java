package io.v4guard.shield.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.v4guard.connector.common.CoreInstance;
import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.api.platform.ShieldPlatform;

public class ShieldCommon {

    private ShieldAPI shieldAPI;
    private final ShieldPlatform shieldPlatform;
    private final ObjectMapper objectMapper;

    public ShieldCommon(ShieldPlatform platform) {
        this.shieldPlatform = platform;
        this.objectMapper = new ObjectMapper();
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
        CoreInstance.get().getRemoteConnection().send(
                "accshield:login",
                objectMapper.convertValue(auth, ObjectNode.class)
        );
    }

}
