package br.com.caminhodasaguas.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationDTO(
        @NotBlank(message = "Email é obrigatório.")
        String email
) {}