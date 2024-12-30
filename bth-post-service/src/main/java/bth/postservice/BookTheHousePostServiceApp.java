package bth.postservice;

import bth.common.rabbitmq.RabbitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(RabbitConfig.class)
public class BookTheHousePostServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(BookTheHousePostServiceApp.class, args);
    }

}
