package bth.ui.controller;

import bth.ui.service.RedisWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final RedisWrapper redisWrapper;

    @PostMapping("/notification/token")
    @ResponseBody
    public String saveNotificationToken(@RequestBody Map<String, String> data) {
        redisWrapper.set("notification_token", data.get("token"));
        return "Token saved";
    }
}
