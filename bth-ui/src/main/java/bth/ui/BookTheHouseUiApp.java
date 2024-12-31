package bth.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "bth.*")
@EnableWebSecurity
@EnableAspectJAutoProxy
@Slf4j
public class BookTheHouseUiApp {

    public static void main(String[] args) {
        SpringApplication.run(BookTheHouseUiApp.class, args);
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void onContextRefreshed(ContextRefreshedEvent event) {
    }
}
