package bth.notificator.listener;

import bth.common.message.PostCreatedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostSubsListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(id = "bth-notificator-post-subs-listener",
                    queues = "${bth.rabbit.queue.post-subs-email-queue}")
    public void listen(PostCreatedMessage message) {
        log.info("Received post created message: {}", message);
        // send email logic
    }
}
