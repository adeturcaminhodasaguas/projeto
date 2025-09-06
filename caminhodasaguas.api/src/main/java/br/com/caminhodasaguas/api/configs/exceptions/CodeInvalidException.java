package br.com.caminhodasaguas.api.configs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CodeInvalidException extends RuntimeException {
    public CodeInvalidException(String message) {
        super(message);
    }
}
