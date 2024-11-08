package io.v4guard.shield.common.api;

import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.service.ConnectedCounterService;
import org.jetbrains.annotations.Nullable;

public class DefaultShieldAPI implements ShieldAPI {

    private ConnectedCounterService connectedCounterService;

    @Override
    public @Nullable ConnectedCounterService getConnectedCounterService() {
        return connectedCounterService;
    }

    @Override
    public void setConnectedCounterService(ConnectedCounterService connectedCounterService) {
        this.connectedCounterService = connectedCounterService;
    }
}
