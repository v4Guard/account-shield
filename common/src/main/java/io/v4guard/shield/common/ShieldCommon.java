package io.v4guard.shield.common;

import io.v4guard.connector.common.CoreInstance;
import io.v4guard.connector.common.accounts.auth.Authentication;
import io.v4guard.shield.api.ShieldAPI;
import io.v4guard.shield.api.v4GuardShieldProvider;
import io.v4guard.shield.common.api.DefaultShieldAPI;
import io.v4guard.shield.common.mode.ShieldMode;

public class ShieldCommon {

    private static ShieldCommon INSTANCE;
    private final ShieldAPI shieldAPI;
    private final ShieldMode shieldMode;

    public ShieldCommon(ShieldMode mode) {
        INSTANCE = this;
        this.shieldMode = mode;
        this.shieldAPI = new DefaultShieldAPI();

        v4GuardShieldProvider.register(shieldAPI);
    }

    public static ShieldCommon get() {
        return INSTANCE;
    }

    public ShieldMode getShieldMode() {
        return shieldMode;
    }

    public ShieldAPI getShieldAPI() {
        return shieldAPI;
    }

    public void sendMessage(Authentication auth) {
        CoreInstance.get().getAccountShieldSender().sendSocketMessage(auth);
    }

}
