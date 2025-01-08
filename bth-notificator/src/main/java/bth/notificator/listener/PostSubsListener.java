package bth.notificator.listener;

import bth.common.rabbitmq.message.PostCreatedMessage;
import bth.notificator.service.EmailService;
import bth.notificator.service.HtmlTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostSubsListener {

    private final EmailService emailService;
    private final HtmlTemplateService htmlTemplateService;

    @RabbitListener(id = "bth-notificator-post-subs-listener",
            queues = "${bth.rabbit.queue.post-subs-email-queue}")
    public void listen(Message message, @Payload PostCreatedMessage payload) {
        MDC.put("correlationId", message.getMessageProperties().getCorrelationId());
        log.info("Received post created message: {}", payload);

        if (!emailService.isWhiteListed(payload.receiverEmail())) {
            log.debug("Receiver email is not allowed: {}, skip sending email notification", payload.receiverEmail());
            return;
        }

        var htmlContent = htmlTemplateService.loadTemplate(
                "postCreatedEmail",
                Map.of("userId", payload.creatorId(), "postTitle", payload.postTitle()));
        emailService.sendHtmlEmail(payload.receiverEmail(), "New Post Created", htmlContent);

        MDC.clear();
    }
}
