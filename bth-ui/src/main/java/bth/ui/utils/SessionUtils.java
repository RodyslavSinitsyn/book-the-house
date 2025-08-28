package bth.ui.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class SessionUtils {

    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(a -> a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken))
                .orElse(false);
    }

    public String getUserEmailIfPresent() {
        if (!isAuthenticated()) {
            return getUsername();
        }
        return getCurrentAppUser().getEmail();
    }

    public String getUsername() {
        return isAuthenticated()
                ? getCurrentAppUser().getFullName()
                : "anonymousUser";
    }

    public CurrentAppUser getCurrentAppUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CurrentAppUser currentAppUser) {
            return currentAppUser;
        }
        return null;
    }
}
