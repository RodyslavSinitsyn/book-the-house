package bth.notificator;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableRabbit
public class BookTheHouseNotificatorApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BookTheHouseNotificatorApp.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
