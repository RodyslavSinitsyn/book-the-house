package bth.ui.controller;

import bth.common.models.chat.Chat;
import bth.common.models.chat.ChatMessage;
import bth.ui.service.RedisWrapper;
import bth.ui.utils.DateUtils;
import bth.ui.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisWrapper redisWrapper;

    @GetMapping("/chat/list")
    public String chatList(Model model) {
        List<Chat> chats = new ArrayList<>();
        Map<String, String> map = redisWrapper.getJedis().hgetAll(redisWrapper.getCacheKey("chat"));
        map.entrySet().forEach(entry -> {
            var chat = new Chat();
            chat.setId(entry.getValue());
            chat.setRecipient(entry.getKey());
            chats.add(chat);
        });
        chats.forEach(chat -> {
            var chatMessages = redisWrapper.globalGetList("chat_" + chat.getId(), ChatMessage.class);
            if (chatMessages.isEmpty()) {
                chat.setHide(true);
                return;
            }
            int unreadCount = (int) chatMessages.stream()
                    .filter(m -> m.getRecipientId().equals(SessionUtils.getUsername()))
                    .filter(m -> !m.isRead())
                    .count();
            chat.setUnreadCount(unreadCount);
            Optional.ofNullable(CollectionUtils.lastElement(chatMessages))
                    .ifPresent(lastMessage -> {
                        chat.setLastMessage(lastMessage.getText());
                        chat.setLastMessageTime(DateUtils.toDateTime(lastMessage.getTimestamp()));
                    });
        });
        model.addAttribute("chats", chats);
        return "chat/chats";
    }

    @GetMapping("/chat/{chatId}/{recipientId}")
    public String chat(@PathVariable("chatId") String chatId,
                       @PathVariable("recipientId") String recipientId,
                       Model model) {
        var chatExist = redisWrapper.getJedis().exists("chat_" + chatId);
        if (!chatExist) {
            redisWrapper.globalSetList("chat_" + chatId, Collections.emptyList(), Duration.ZERO); // Init empty chat
            // Set key for sender
            redisWrapper.getJedis().hset(redisWrapper.getCacheKey("chat"), recipientId, chatId);
            // Set key for recipient
            redisWrapper.getJedis().hset(redisWrapper.getCacheKey("chat", recipientId), SessionUtils.getUsername(), chatId);

            model.addAttribute("messages", Collections.emptyList());
        } else {
            var chatMessages = readMessages(
                    chatId,
                    m -> m.getRecipientId().equals(SessionUtils.getUsername()));
            model.addAttribute("messages", chatMessages);
        }
        model.addAttribute("chatId", chatId);
        model.addAttribute("recipientId", recipientId);
        model.addAttribute("senderId", SessionUtils.getUsername());
        return "chat/chat";
    }

    @PutMapping("/chat/read")
    @ResponseBody
    public String readMessages(@RequestParam("chatId") String chatId,
                               @RequestParam("messageId") String messageId) {
        readMessages(chatId, m -> m.getMessageId().equals(messageId));
        return "Message %s mark as read".formatted(messageId);
    }

    private List<ChatMessage> readMessages(String chatId,
                                           Predicate<ChatMessage> messagePredicate) {
        var messages = redisWrapper.globalGetList("chat_" + chatId, ChatMessage.class);
        messages.stream()
                .filter(messagePredicate)
                .forEach(message -> message.setRead(true));
        redisWrapper.globalSetList("chat_" + chatId, messages, Duration.ZERO);
        return messages;
    }
}
