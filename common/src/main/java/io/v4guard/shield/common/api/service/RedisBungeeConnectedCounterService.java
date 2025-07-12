package io.v4guard.shield.common.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.v4guard.shield.api.service.ConnectedCounterService;
import io.v4guard.shield.common.ShieldCommon;
import io.v4guard.shield.common.redis.UserStateUpdateRedisMessage;
import io.v4guard.shield.common.redis.type.OperationType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedisBungeeConnectedCounterService implements ConnectedCounterService {

    /*
        Soo RedisBungee does not support getting a list of connected accounts via the API
        This is a workaround to get the same functionality without doing internal modifications to RedisBungee or
        rewriting the whole plugin or the backend

        We depend on the Events from RedisBungee to get the connected accounts, and we store them in the map.
        If the player is not in the map, we add them, if they are in the map, we remove them

        If you know a better way to do this, please open a pull request!!!
     */
    private final ConcurrentHashMap<InetAddress, Set<String>> ipAddresses;
    private final ShieldCommon shieldCommon;

    public RedisBungeeConnectedCounterService(ShieldCommon shieldCommon) {
        this.shieldCommon = shieldCommon;
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
        Set<String> accounts = ipAddresses.get(ipAddress);
        if (accounts == null) return 0;
        return accounts.size();
    }

    @Override
    public int getConnectedAccounts(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            Set<String> accounts = ipAddresses.get(address);
            if (accounts == null) return 0;
            return accounts.size();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void add(InetAddress ipAddress, String account) {
        Set<String> accounts = ipAddresses.getOrDefault(ipAddress, new HashSet<>());

        accounts.add(account);
        ipAddresses.put(ipAddress, accounts);
    }


    public void remove(InetAddress ipAddress, String account) {
        Set<String> accounts = ipAddresses.get(ipAddress);

        if (accounts == null) return;

        accounts.remove(account);
        ipAddresses.put(ipAddress, accounts);
    }

    public void handleMessage(String message) {
        try {
            UserStateUpdateRedisMessage data = shieldCommon.getObjectMapper().readValue(message, UserStateUpdateRedisMessage.class);
            if (data == null) {
                // ! Invalid message format, ignore
                return;
            }

            Set<String> accounts = ipAddresses.getOrDefault(data.ipAddress(), new HashSet<>());
            switch (data.operationType()) {
                case JOIN:
                    accounts.add(data.username());
                    break;
                case QUIT:
                    accounts.remove(data.username());
                    break;
                default:
                    // ! Unsupported operation type, ignore
                    return;
            }

            ipAddresses.put(data.ipAddress(), accounts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    @Override
    public Collection<String> getConnectedAccounts(InetAddress ipAddress, int limit) {
        Set<String> accounts = ipAddresses.get(ipAddress);
        if (accounts == null) return Collections.emptyList();

        return accounts.stream().limit(limit).toList();
    }

    @Override
    public Collection<String> getConnectedAccounts(String ipAddress, int limit) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            Set<String> accounts = ipAddresses.get(address);
            if (accounts == null) return Collections.emptyList();

            return accounts.stream().limit(limit).toList();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
