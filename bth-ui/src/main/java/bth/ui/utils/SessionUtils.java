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

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getAuthenticatedUserCacheKey() {
        return "user_" + getUsername();
    }
}
