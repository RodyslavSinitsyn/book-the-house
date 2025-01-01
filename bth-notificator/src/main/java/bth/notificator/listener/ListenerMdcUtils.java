package bth.notificator.listener;

import org.slf4j.MDC;
import org.springframework.amqp.core.Message;

public class ListenerMdcUtils {

    public static void populateMdcContext(Message message) {
        MDC.put("correlationId", message.getMessageProperties().getCorrelationId());
    }
}
