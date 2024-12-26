package bth.ui.service;

import bth.ui.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisWrapper {

    public static final String CACHE_KEY_FORMAT = "%s_%s";

    private final Jedis jedis;

    public String get(String key) {
        return jedis.get(getCacheKey(key));
    }

    public <T> String getOrDefault(String keyField, T defaultValue) {
        return Optional.ofNullable(get(keyField)).orElse(String.valueOf(defaultValue));
    }

    public void set(String keyField, String value) {
        set(keyField, value, -1);
    }

    public void set(String key, String value, int seconds) {
        jedis.set(getCacheKey(key), value);
        if (seconds > 0) {
            jedis.expire(getCacheKey(key), seconds);
        }
    }

    private String getCacheKey(String cacheKey) {
        return String.format(CACHE_KEY_FORMAT, SessionUtils.getAuthenticatedUserCacheKey(), cacheKey);
    }

}
