package bth.ui.controller;

import bth.ui.service.RedisWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> saveNotificationToken(@RequestBody Map<String, String> data) {
        if (!data.containsKey("token")) {
            return ResponseEntity.badRequest().body("Token is required");
        }
        redisWrapper.set("notification_token", data.get("token"));
        return ResponseEntity.ok("Token saved");
    }
}
