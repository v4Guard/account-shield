package io.v4guard.shield.core.auth;

import org.bson.Document;

public class ConnectorAuthentication {

    private final String username;
    private final io.v4guard.plugin.core.accounts.auth.AuthType authType;
    private boolean hasPermission;

    public ConnectorAuthentication(String username, AuthType authType, boolean hasPermission) {
        this.username = username;
        switch (authType) {
            case LOGIN:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.LOGIN;
                break;
            case REGISTER:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.REGISTER;
                break;
            default:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.UNKNOWN;
                break;
        }
        this.hasPermission = hasPermission;
    }

    public ConnectorAuthentication(Authentication auth){
        this.username = auth.getUsername();
        switch (auth.getAuthType()) {
            case LOGIN:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.LOGIN;
                break;
            case REGISTER:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.REGISTER;
                break;
            default:
                this.authType = io.v4guard.plugin.core.accounts.auth.AuthType.UNKNOWN;
                break;
        }
        this.hasPermission = auth.hasPermission();
    }

    public String getUsername() {
        return username;
    }

    public io.v4guard.plugin.core.accounts.auth.AuthType getAuthType() {
        return authType;
    }

    public io.v4guard.plugin.core.accounts.auth.Authentication toAuthentication(){
        return new io.v4guard.plugin.core.accounts.auth.Authentication(username, authType, hasPermission);
    }

    public boolean hasPermission() {
        return hasPermission;
    }

    public static void sendMessage(ConnectorAuthentication auth){
        io.v4guard.plugin.core.v4GuardCore.getInstance().getAccountShieldManager().sendSocketMessage(auth.toAuthentication());
    }

    public Document serialize(){
        return new Document("username", username)
                .append("authType", authType.toString())
                .append("hasPermission", hasPermission);
    }
}
