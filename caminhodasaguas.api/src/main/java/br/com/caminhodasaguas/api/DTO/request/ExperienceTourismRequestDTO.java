package br.com.caminhodasaguas.api.DTO.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record ExperienceTourismRequestDTO(
        String name,
        String description,
        MultipartFile img,
        String phone,
        String instagram,
        String site,
        List<MultipartFile> highlights) {
}
