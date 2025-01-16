package io.v4guard.shield.api.service;

import java.net.InetAddress;
import java.util.Collection;

public interface ConnectedCounterService {

    /**
     * Check if there are any accounts online
     * @param ipAddress the ip address to check
     * @return true if there are any accounts online
     */
    boolean hasAnyAccountOnline(InetAddress ipAddress);

    /**
     * Check if there are any accounts online
     * @param ipAddress the ip address to check
     * @return true if there are any accounts online
     */
    boolean hasAnyAccountOnline(String ipAddress);

    /**
     * Get the amount of connected accounts
     * @param ipAddress the ip address to check
     * @return the amount of connected accounts
     */
    int getConnectedAccounts(InetAddress ipAddress);

    /**
     * Get the amount of connected accounts
     * @param ipAddress the ip address to check
     * @return the amount of connected accounts
     */
    int getConnectedAccounts(String ipAddress);

    /**
     * Get the names of the connected accounts
     * @param ipAddress the ip address to check
     * @param limit the limit of accounts to return
     * @return the amount of connected accounts
     */
    Collection<String> getConnectedAccounts(InetAddress ipAddress, int limit);

    /**
     * Get the names of the connected accounts
     * @param ipAddress the ip address to check
     * @param limit the limit of accounts to return
     * @return the amount of connected accounts
     */
    Collection<String> getConnectedAccounts(String ipAddress, int limit);
}
