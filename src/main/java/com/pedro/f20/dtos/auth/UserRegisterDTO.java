package com.pedro.f20.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
    @NotBlank
    String username,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {}