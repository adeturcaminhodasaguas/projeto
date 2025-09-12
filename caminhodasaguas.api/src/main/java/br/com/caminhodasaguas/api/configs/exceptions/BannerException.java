package br.com.caminhodasaguas.api.configs.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BannerException extends RuntimeException {
    public BannerException(String message) {
        super(message);
    }
}
