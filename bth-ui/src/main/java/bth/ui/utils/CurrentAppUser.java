package bth.ui.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class CurrentAppUser extends AbstractAuthenticationToken {
    private final String externalId;
    private final String email;
    private final String login;
    private final String fullName;
    private final String avatarUrl;

    public CurrentAppUser(String externalId, String email, String login,
                          String fullName, String avatarUrl, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.externalId = externalId;
        this.email = email;
        this.login = login;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public Object getCredentials() {
        return externalId;
    }

    @Override
    public Object getPrincipal() {
        return login;
    }

    @Override
    public String getName() {
        return fullName;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
