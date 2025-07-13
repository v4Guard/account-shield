package io.v4guard.shield.common.redis;

import io.v4guard.shield.common.redis.type.OperationType;

import java.net.InetAddress;

public record UserStateUpdateRedisMessage(
        OperationType operationType,
        InetAddress ipAddress,
        String username
) {}
