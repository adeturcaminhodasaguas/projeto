package br.com.caminhodasaguas.api.DTO.request;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BannerRequestDTO(
        @NotNull(message = "Imagem é obrigatória") MultipartFile img,

        @NotNull(message = "Posição é obrigatória") Integer position,

        @Size(min = 1, message = "Link não pode ser vazio.")
        @URL(message = "Link deve ser uma URL válida.")
        String link,

        @NotBlank(message = "Texto é obrigatório")
        String altText
) {}