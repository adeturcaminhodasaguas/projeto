package br.com.caminhodasaguas.api.DTO;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;
import java.util.UUID;

import br.com.caminhodasaguas.api.domains.enums.UserEnum;

public record UserDTO(
        UUID id,

        @NotBlank(message = "Nome é obrigatório.")
        String name,

        @NotBlank(message = "Email é obrigatório.")
        String email,

        @NotBlank(message = "Documento é obrigatório.")
        String document,

        @NotBlank(message = "Telefone é obrigatório.")
        String phone,

        UserEnum role,

        OffsetDateTime deletedAt
) {}