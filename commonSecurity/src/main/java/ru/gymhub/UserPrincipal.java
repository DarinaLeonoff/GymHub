package ru.gymhub;

import java.security.Principal;

public record UserPrincipal(
        long userId,
        String accountType,
        String role,
        String email
) implements Principal {
    @Override
    public String getName() {
        return email;
    }
}
