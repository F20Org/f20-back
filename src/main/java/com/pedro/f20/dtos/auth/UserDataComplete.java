package com.pedro.f20.dtos.auth;

public record UserDataComplete (
    Long id,
    String username,
    String email,
    Boolean isActive
) {
}
