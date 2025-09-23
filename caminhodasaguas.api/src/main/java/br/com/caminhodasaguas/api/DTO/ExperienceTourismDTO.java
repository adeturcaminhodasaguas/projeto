package br.com.caminhodasaguas.api.DTO;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExperienceTourismDTO(
        UUID id,
        @NotBlank(message = "Nome é obrigatório.") String name,

        @NotBlank(message = "Descrição é obrigatório.") String description,

        @NotBlank(message = "Telefone é obrigatório.") String phone,

        @Size(min = 1, message = "Instagram, não pode ser vazio.") @URL(message = "Instagram, deve ser uma URL válida.") String instagram,

        @Size(min = 1, message = "Site, não pode ser vazio.") @URL(message = "Site, se informado, deve ser uma URL válida.") String site,

        List<ItemCustomDTO> highlights,

        @JsonProperty(access = Access.WRITE_ONLY) List<MultipartFile> new_highlights,

        @JsonProperty(access = Access.WRITE_ONLY) List<UUID> deleted_highlights
) {}
