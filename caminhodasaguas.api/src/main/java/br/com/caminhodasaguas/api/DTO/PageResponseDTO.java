package br.com.caminhodasaguas.api.DTO;

import java.util.List;

public record PageResponseDTO<T>(
         List<T> data,
         int page,
         int size,
         long totalElements,
         int totalPages,
         boolean last
) {}
