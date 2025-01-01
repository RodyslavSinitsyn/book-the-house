package bth.ui.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Optional;

@UtilityClass
public class SessionUtils {

    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(a -> a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken))
                .orElse(false);
    }

    public String getFriendlyUsername() {
        if (!isAuthenticated()) {
            return getUsername();
        }
        var principal = (OAuth2AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (String) principal.getAttributes().get("name");
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getAuthenticatedUserCacheKey() {
        return "user_" + getUsername();
    }
}
