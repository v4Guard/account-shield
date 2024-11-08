package io.v4guard.shield.common.hook;

public abstract class AuthenticationHook {

    private final String hookName;

    public AuthenticationHook(String hookName) {
        this.hookName = hookName;
    }

    public String getHookName() {
        return hookName;
    }
}
