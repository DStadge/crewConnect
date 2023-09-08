package de.iav.frontend.security;

public record AppUserResponse(
        String id,
        String username,

        String email,
        String password,
        String role

) {
}
