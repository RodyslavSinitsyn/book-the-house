package bth.ui.service;

import bth.ui.utils.SessionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisWrapper {

    public static final String CACHE_KEY_FORMAT = "user_%s_%s";

    @Getter
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

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

    @SneakyThrows
    public <T> void globalSetListWithTtlCheck(String key,
                                              @NonNull List<T> values,
                                              Duration ttl) {
        if (jedis.exists(key)) {
            return;
        }
        globalSetList(key, values, ttl);
    }

    @SneakyThrows
    public <T> void globalSetList(String key,
                                  @NonNull List<T> values,
                                  Duration ttl) {
        var listAsJson = objectMapper.writeValueAsString(values);
        jedis.set(key, listAsJson);
        if (ttl.toSeconds() > 0) {
            jedis.expire(key, ttl.toSeconds());
        }
    }

    @SneakyThrows
    public <T> List<T> globalGetList(String key, Class<T> clazz) {
        var valueAsString = jedis.get(key);
        if (valueAsString == null) {
            return Collections.emptyList();
        }
        return objectMapper.readValue(valueAsString, new TypeReference<>() {
            @Override
            public java.lang.reflect.Type getType() {
                return objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            }
        });
    }

    public String getCacheKey(String cacheKey) {
        return getCacheKey(cacheKey, SessionUtils.getUsername());
    }

    public String getCacheKey(String cacheKey, String username) {
        return String.format(CACHE_KEY_FORMAT, username, cacheKey);
    }
}
