package bth.ui.converter;

import bth.ui.utils.CurrentAppUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public interface OAuth2ToCurrentAppUserConverter extends Converter<OAuth2AuthenticatedPrincipal, CurrentAppUser> {
    String externalSystemName();
}
