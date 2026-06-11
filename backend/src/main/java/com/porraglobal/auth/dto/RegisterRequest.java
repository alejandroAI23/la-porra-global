package com.porraglobal.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Solo letras, números y guiones bajos")
        String username,

        @NotBlank @Email @Size(max = 120)
        String email,

        @NotBlank @Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @Size(max = 80)
        String displayName
) {
}
