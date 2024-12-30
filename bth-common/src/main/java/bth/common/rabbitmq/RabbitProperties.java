package bth.common.rabbitmq;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bth.rabbit")
@Getter
@Setter
@NoArgsConstructor
public class RabbitProperties {

    private Queue queue;
    private Exchange exchange;

    @Data
    public static class Queue {
        private String postSubsEmailQueue;
    }

    @Data
    public static class Exchange {
        private String postSubsEmailExchange;
    }
}
