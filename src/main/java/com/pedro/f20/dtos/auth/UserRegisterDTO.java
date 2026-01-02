package com.pedro.f20.dtos.auth;

public record UserRegisterDTO(
    String username,
    String email,
    String password
) {}