package br.com.caminhodasaguas.api.DTO.customs;

import java.util.UUID;

public record ItemCustomDTO(
        UUID id,
        String img
) {}