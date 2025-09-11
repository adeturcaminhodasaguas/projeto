package br.com.caminhodasaguas.api.configs.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExperienceTourismNotFoundException extends RuntimeException {
    public ExperienceTourismNotFoundException(String message) {
        super(message);
    }
}
