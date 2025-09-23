package br.com.caminhodasaguas.api.DTO;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;

public record ImageDTO(
    List<ItemCustomDTO> highlights,
    List<MultipartFile> new_highlights,
    List<UUID> deleted_highlights
) {}
