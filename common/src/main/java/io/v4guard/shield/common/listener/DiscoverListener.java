package io.v4guard.shield.common.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.v4guard.connector.api.socket.SocketMessageListener;
import io.v4guard.shield.common.ShieldCommon;

import java.util.Collection;
import java.util.function.Consumer;

public class DiscoverListener extends SocketMessageListener {

    private ShieldCommon shieldCommon;

    public DiscoverListener(String eventName, ShieldCommon shieldCommon) {
        super(eventName);

        this.shieldCommon = shieldCommon;
    }

    @Override
    public Consumer<Object[]> onEvent() {
        return objects -> {
            try {
                ObjectNode node = shieldCommon.getObjectMapper().readValue(objects[0].toString(), ObjectNode.class);

                String ip = node.get("ip").asText();

                Collection<String> connectedAccounts = shieldCommon.getShieldAPI().getConnectedCounterService().getConnectedAccounts(ip, Integer.parseInt(shieldCommon.getAddon().getSettings().get("accountLimit")));

                ObjectNode response = shieldCommon.getObjectMapper().createObjectNode();
                response.set("accounts", shieldCommon.getObjectMapper().valueToTree(connectedAccounts));
                response.put("taskID", node.get("taskID").asText());

                shieldCommon.sendMessage("accshield:discover", response.toString());
            } catch (JsonProcessingException e) {
                // ! todo log error
            }
        };
    }
}
