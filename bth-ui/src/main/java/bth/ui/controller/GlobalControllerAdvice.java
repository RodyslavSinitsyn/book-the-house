package bth.ui.controller;

import bth.ui.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${bth.ui.firebase.apiKey}")
    private String firebaseApiKey;

    @Value("${bth.ui.firebase.vapidKey}")
    private String firebaseVapidKey;

    @ModelAttribute
    public void setModelAttributes(Model model) {
        model.addAttribute("isAuth", SessionUtils.isAuthenticated());
        model.addAttribute("firebaseApiKey", firebaseApiKey);
        model.addAttribute("firebaseVapidKey", firebaseVapidKey);
    }
}
