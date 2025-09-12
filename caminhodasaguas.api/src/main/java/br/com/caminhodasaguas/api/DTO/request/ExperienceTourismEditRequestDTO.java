package br.com.caminhodasaguas.api.DTO.request;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record ExperienceTourismEditRequestDTO(
        String name,
        String description,
        MultipartFile img,
        String phone,
        String instagram,
        String site,
        List<MultipartFile> highlights,
        List<UUID> highlights_exists) {
}
