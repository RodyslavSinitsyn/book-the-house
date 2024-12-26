package bth.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import redis.clients.jedis.Jedis;

@SpringBootApplication
@EnableWebSecurity
@EnableAspectJAutoProxy
@Slf4j
public class BookTheHouseUiApp {

    @Autowired
    private Jedis jedis;

    public static void main(String[] args) {
        SpringApplication.run(BookTheHouseUiApp.class, args);
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void onContextRefreshed(ContextRefreshedEvent event) {
        log.info(jedis.clientInfo());
    }
}
