package bth.ui.controller;

import bth.common.models.chat.ChatMessage;
import bth.ui.service.RedisWrapper;
import bth.ui.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisWrapper redisWrapper;

    @GetMapping("/chat/{chatId}/{recipientId}")
    public String chat(@PathVariable("chatId") String chatId,
                       @PathVariable("recipientId") String recipientId,
                       Model model) {
        var chatExist = redisWrapper.getJedis().exists("chat_" + chatId);
        if (!chatExist) {
            redisWrapper.globalSetList("chat_" + chatId, Collections.emptyList(), Duration.ZERO); // Init empty chat
            redisWrapper.set("chat_" + recipientId, chatId);
            model.addAttribute("messages", Collections.emptyList());
        } else {
            var chatMessages = redisWrapper.globalGetList("chat_" + chatId, ChatMessage.class);
            model.addAttribute("messages", chatMessages);
        }
        model.addAttribute("chatId", chatId);
        model.addAttribute("recipientId", recipientId);
        model.addAttribute("senderId", SessionUtils.getUsername());
        return "post/chat";
    }
}
