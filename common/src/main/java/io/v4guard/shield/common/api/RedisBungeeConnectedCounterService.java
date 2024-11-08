package io.v4guard.shield.common.api;

import com.imaginarycode.minecraft.redisbungee.AbstractRedisBungeeAPI;
import io.v4guard.shield.api.service.ConnectedCounterService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RedisBungeeConnectedCounterService implements ConnectedCounterService {

    private final ConcurrentHashMap<InetAddress, Integer> ipAddresses;

    public RedisBungeeConnectedCounterService() {
        ipAddresses = new ConcurrentHashMap<>();
    }

    @Override
    public boolean hasAnyAccountOnline(InetAddress ipAddress) {
        return ipAddresses.containsKey(ipAddress);
    }

    @Override
    public boolean hasAnyAccountOnline(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return ipAddresses.containsKey(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getConnectedAccounts(InetAddress ipAddress) {
        return ipAddresses.getOrDefault(ipAddress, 0);
    }

    @Override
    public int getConnectedAccounts(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return ipAddresses.getOrDefault(address, 0);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void add(InetAddress ipAddress) {
        int value = ipAddresses.getOrDefault(ipAddress, 0);

        ipAddresses.put(ipAddress, value+ 1);
    }


    public void remove(InetAddress ipAddress) {
        int value = ipAddresses.getOrDefault(ipAddress, 0) - 1;

        if (value <= 0) {
            ipAddresses.remove(ipAddress);
            return;
        }

        ipAddresses.put(ipAddress, value);
    }

    @Override
    public Collection<String> getConnectedAccounts(InetAddress ipAddress, int limit) {
        throw new UnsupportedOperationException("RedisBungee does not support getting a list of connected accounts");
    }

    @Override
    public Collection<String> getConnectedAccounts(String ipAddress, int limit) {
        throw new UnsupportedOperationException("RedisBungee does not support getting a list of connected accounts");
    }
}
