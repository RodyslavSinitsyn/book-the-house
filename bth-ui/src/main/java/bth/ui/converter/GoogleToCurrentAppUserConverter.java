package bth.ui.converter;

import bth.ui.utils.CurrentAppUser;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

@Component
public class GoogleToCurrentAppUserConverter implements OAuth2ToCurrentAppUserConverter {

    @Override
    public CurrentAppUser convert(OAuth2AuthenticatedPrincipal principal) {
        return new CurrentAppUser(
                principal.getAttribute("sub"),
                principal.getAttribute("email"),
                principal.getAttribute("email"),
                principal.getAttribute("name"),
                principal.getAttribute("picture"),
                principal.getAuthorities());
    }

    @Override
    public String externalSystemName() {
        return "google";
    }
}
