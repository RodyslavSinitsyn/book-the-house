package bth.notificator.listener;

import bth.common.rabbitmq.message.PostCreatedMessage;
import bth.notificator.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostSubsListener {

    private final EmailService emailService;

    @RabbitListener(id = "bth-notificator-post-subs-listener",
            queues = "${bth.rabbit.queue.post-subs-email-queue}")
    public void listen(Message message, @Payload PostCreatedMessage payload) {
        MDC.put("correlationId", message.getMessageProperties().getCorrelationId());
        log.info("Received post created message: {}", payload);

        if (!emailService.isWhiteListed(payload.receiverEmail())) {
            log.debug("Receiver email is not allowed: {}, skip sending email notification", payload.receiverEmail());
            return;
        }
        emailService.sendSimpleEmail(payload.receiverEmail(),
                "Book the House",
                """
                        New post has been published!
                        Title: %s.
                        Creator: %s
                        """.formatted(payload.postTitle(), payload.creatorId()));
        MDC.clear();
    }
}
