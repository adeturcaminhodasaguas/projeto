package br.com.caminhodasaguas.api.DTO.customs;

import java.util.UUID;

public record BannerCustomDTO(
    UUID id,
    String img,
    Integer position,
    String link,
    String altText 
) {}
