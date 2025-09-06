package br.com.caminhodasaguas.api.DTO.customs;

import java.util.List;
import java.util.UUID;

public record MunicipalityCustomDTO(
        UUID id,
        String name,
        String description,
        String img,
        String phone,
        String instagram,
        String site,
        List<ItemCustomDTO> highlights
) {}
