package bth.ui.controller;

import bth.common.contract.PostSubscriptionService;
import bth.ui.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostSubscriptionController {

    private final PostSubscriptionService postSubscriptionService;

    @GetMapping("/subscriptions")
    public String subscriptions(Model model) {
        var userSubscriptions = postSubscriptionService.subscriptions(SessionUtils.getUsername(), null);
        model.addAttribute("subscriptions", userSubscriptions);
        return "subscriptions/subscriptions";
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public String subscribe(@RequestParam(value = "email", required = false) String email,
                            @RequestParam("subscribedUserId") String subscribedUserId) {
        postSubscriptionService.subscribe(subscribedUserId, email,
                SessionUtils.isAuthenticated() ? SessionUtils.getUsername() : null);
        return "subscribed";
    }


    @PostMapping("/unsubscribe")
    @ResponseBody
    public String unsubscribe(@RequestParam(value = "email", required = false) String email,
                              @RequestParam("subscribedUserId") String subscribedUserId) {
        postSubscriptionService.unsubscribe(subscribedUserId, email,
                SessionUtils.isAuthenticated() ? SessionUtils.getUsername() : null);
        return "unsubscribed";
    }
}
