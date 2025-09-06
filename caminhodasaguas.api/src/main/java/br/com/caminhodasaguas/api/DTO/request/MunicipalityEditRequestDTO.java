package br.com.caminhodasaguas.api.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record MunicipalityEditRequestDTO(
        @NotBlank(message = "Nome é obrigatório.")
        String name,

        @NotBlank(message = "Descrição é obrigatório.")
        String description,

        String img_exist,

        MultipartFile img,

        @NotBlank(message = "Telefone é obrigatório.")
        String phone,

        @Size(min = 1, message = "Instagram, não pode ser vazio.")
        @URL(message = "Instagram, deve ser uma URL válida.")
        String instagram,

        @Size(min = 1, message = "Site, não pode ser vazio.")
        @URL(message = "Site, se informado, deve ser uma URL válida.")
        String site,

        List<UUID> highlights_exists,

        @Size(max = 6, message = "No máximo 6 destaques novos.")
        List<MultipartFile> highlights
) {}
