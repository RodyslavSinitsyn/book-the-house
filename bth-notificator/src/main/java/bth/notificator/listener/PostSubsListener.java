package bth.notificator.listener;

import bth.common.rabbitmq.message.PostCreatedMessage;
import bth.notificator.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostSubsListener {

    private final EmailService emailService;

    @RabbitListener(id = "bth-notificator-post-subs-listener",
            queues = "${bth.rabbit.queue.post-subs-email-queue}")
    public void listen(PostCreatedMessage message) {
        log.info("Received post created message: {}", message);

        if (!emailService.isWhiteListed(message.receiverEmail())) {
            log.debug("Receiver email is not allowed: {}, skip sending email notification", message.receiverEmail());
            return;
        }
        emailService.sendSimpleEmail(message.receiverEmail(),
                "Book the House",
                """
                        New post has been published!
                        Title: %s.
                        Creator: %s
                        """.formatted(message.postTitle(), message.creatorId()));
    }
}
