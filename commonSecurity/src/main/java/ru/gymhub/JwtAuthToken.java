package ru.gymhub;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class JwtAuthToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public JwtAuthToken(UserPrincipal principal) throws  NullPointerException {
        super(createAuthorities(principal));
        this.principal = principal;
    }

    private static List<SimpleGrantedAuthority> createAuthorities(UserPrincipal principal) throws NullPointerException {
        Objects.requireNonNull(principal, "principal must not be null");
        return List.of(new SimpleGrantedAuthority(principal.role()));
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
