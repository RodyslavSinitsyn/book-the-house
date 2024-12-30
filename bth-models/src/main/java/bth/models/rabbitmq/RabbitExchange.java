package bth.models.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RabbitExchange {
    POST_SUBS_EMAIL_DIRECT("post.subs.email.direct");
    private final String exchangeName;
}
