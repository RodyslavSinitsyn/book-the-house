package bth.ui.controller;

import bth.common.models.chat.ChatMessage;
import bth.ui.service.RedisWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Duration;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisWrapper redisWrapper;

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message,
                            @AuthenticationPrincipal Principal sender) {
        log.debug("Received message: {}", message);
        message.setMessageId(UUID.randomUUID().toString());
        message.setSenderId(sender.getName());
        var chatMessages = redisWrapper.globalGetList("chat_" + message.getChatId(), ChatMessage.class);
        chatMessages.add(message);
        redisWrapper.globalSetList("chat_" + message.getChatId(), chatMessages, Duration.ZERO);
        messagingTemplate.convertAndSendToUser(message.getRecipientId(),
                "/topic/chat/" + message.getChatId(),
                message);
    }
}
