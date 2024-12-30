package bth.notificator;

import bth.common.rabbitmq.RabbitConfig;
import bth.notificator.props.MailProperties;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RabbitConfig.class)
@EnableConfigurationProperties(value = MailProperties.class)
public class BookTheHouseNotificatorApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BookTheHouseNotificatorApp.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
