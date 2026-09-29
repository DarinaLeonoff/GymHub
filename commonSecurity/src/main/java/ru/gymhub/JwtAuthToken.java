package ru.gymhub;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class JwtAuthToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public JwtAuthToken(UserPrincipal principal) {
        super(List.of(new SimpleGrantedAuthority(principal.role())));
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
