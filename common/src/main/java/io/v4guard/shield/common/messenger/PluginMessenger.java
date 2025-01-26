package io.v4guard.shield.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.v4guard.shield.api.auth.Authentication;
import io.v4guard.shield.common.ShieldCommon;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public abstract class PluginMessenger {

    private final ShieldCommon shieldCommon;
    private final ObjectMapper objectMapper;

    public PluginMessenger(ShieldCommon shieldCommon) {
        this.shieldCommon = shieldCommon;
        this.objectMapper = new ObjectMapper();
    }

    protected void parsePluginMessage(byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String data = in.readUTF();
            Authentication auth = objectMapper.readValue(data, Authentication.class);
            shieldCommon.sendMessage(auth);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
