package bth.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Value("${bth.ui.redis.host}")
    private String redisHost;

    @Value("${bth.ui.redis.port:6379}")
    private int redisPort;

    @Bean
    public Jedis jedis() {
        return new Jedis(redisHost, redisPort);
    }
}
