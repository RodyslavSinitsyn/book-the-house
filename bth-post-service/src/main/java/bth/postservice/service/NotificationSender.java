package bth.postservice.service;

import bth.common.rabbitmq.RabbitProperties;
import bth.common.rabbitmq.message.PostCreatedMessage;
import bth.postservice.entity.Post;
import bth.postservice.repo.PostSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSender {

    private final RabbitProperties rabbitProperties;
    private final PostSubscriptionRepository postSubscriptionRepository;
    private final AmqpTemplate amqpTemplate;

    @Async("taskExecutor")
    public void notifySubscribers(Post post) {
        var subscriptionList = postSubscriptionRepository
                .findAllBySubscribedUserIdAndEnabled(post.getUserId(), true);
        log.debug("Found {} subscribers for user {}", subscriptionList.size(), post.getUserId());
        var messages = subscriptionList.stream()
                .map(sub -> new PostCreatedMessage(
                        sub.getSubscribedUserId(),
                        sub.getEmail(),
                        post.getTitle()
                ))
                .toList();
        messages.forEach(this::sendMessage);
    }

    @SneakyThrows
    private void sendMessage(PostCreatedMessage message) {
        amqpTemplate.convertAndSend(rabbitProperties.getExchange().getPostSubsEmailExchange(),
                "post.created",
                message);
    }
}
