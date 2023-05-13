package io.v4guard.shield.core;

import io.v4guard.shield.core.messaging.PluginMessager;
import io.v4guard.shield.core.mode.ShieldMode;

import java.util.logging.Logger;

public class v4GuardShieldCore {

    private static v4GuardShieldCore INSTANCE;
    private PluginMessager messager;
    private Logger logger;

    private ShieldMode shieldMode;

    public static final String pluginVersion = "1.0.2";

    public v4GuardShieldCore(ShieldMode mode) {
        INSTANCE = this;
        this.shieldMode = mode;
        initializeLogger();
    }

    public void initializeLogger(){
        logger = Logger.getLogger("v4Guard");
        logger.setUseParentHandlers(true);
    }

    public Logger getLogger() {
        return logger;
    }

    public ShieldMode getShieldMode() {
        return shieldMode;
    }

    public PluginMessager getMessager() {
        return messager;
    }

    public void setMessager(PluginMessager messager) {
        this.messager = messager;
    }

    public static v4GuardShieldCore getInstance() {
        return INSTANCE;
    }
}
