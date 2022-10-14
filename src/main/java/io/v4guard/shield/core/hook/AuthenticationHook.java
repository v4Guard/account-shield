package io.v4guard.shield.core.hook;

public abstract class AuthenticationHook {

    private String hookName;

    public AuthenticationHook(String hookName) {
        this.hookName = hookName;
    }

    public String getHookName() {
        return hookName;
    }
}
