package io.v4guard.shield.api;

import io.v4guard.shield.api.service.ConnectedCounterService;

public interface ShieldAPI {

    /**
     * Get the connected counter service that is currently used by the shield
     * @return the connected counter service
     */
    ConnectedCounterService getConnectedCounterService();

    /**
     * Set the connected counter service implementation that will be used by the shield
     * @param connectedCounterService the connected counter service
     */
    void setConnectedCounterService(ConnectedCounterService connectedCounterService);
}
