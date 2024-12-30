package bth.models.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RabbitQueue {
    POST_SUBS_EMAIL_QUEUE("post.subs.email.queue");
    private final String queueName;
}
