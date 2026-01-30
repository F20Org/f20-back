package com.pedro.f20.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResentCodeDTO(
    @NotBlank
    @Email
    String email
) {}