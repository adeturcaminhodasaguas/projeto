package br.com.caminhodasaguas.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record CodeVerificationDTO(
        @NotBlank(message = "Email é obrigatório.")
        String email,

        @NotBlank(message = "Código é obrigatório.")
        String code
) {}
