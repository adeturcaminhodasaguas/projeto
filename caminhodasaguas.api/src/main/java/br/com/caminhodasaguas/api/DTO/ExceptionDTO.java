package br.com.caminhodasaguas.api.DTO;

import java.time.OffsetDateTime;

public record ExceptionDTO<T>(
        Integer code,
        T details,
        OffsetDateTime dateTime
) {}
