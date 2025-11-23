package bth.ui.converter;

import bth.ui.utils.CurrentAppUser;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GithubToCurrentAppUserConverter implements OAuth2ToCurrentAppUserConverter {

    @Override
    public CurrentAppUser convert(OAuth2AuthenticatedPrincipal principal) {
        return new CurrentAppUser(
                Optional.ofNullable(principal.getAttribute("id"))
                        .map(String::valueOf)
                        .orElseThrow(),
                principal.getAttribute("email"),
                principal.getAttribute("login"),
                principal.getAttribute("name"),
                principal.getAttribute("avatar_url"),
                principal.getAuthorities());
    }

    @Override
    public String externalSystemName() {
        return "github";
    }
}
