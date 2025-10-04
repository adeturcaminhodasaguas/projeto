package br.com.caminhodasaguas.api.DTO;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestimonialsDTO(
        UUID id,
        @NotBlank(message = "Nome é obrigatório.") String name,

        @NotBlank(message = "Descrição é obrigatório.") String description,

        @NotBlank(message = "Cidade é obrigatório.") String city,

        @NotNull(message = "Estrelas é obrigatório.") Integer stars,

        String url,

        @JsonProperty(access = Access.WRITE_ONLY) MultipartFile img
) {}
