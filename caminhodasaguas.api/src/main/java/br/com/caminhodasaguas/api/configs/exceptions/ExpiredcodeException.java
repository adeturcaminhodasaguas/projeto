package br.com.caminhodasaguas.api.configs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpiredcodeException extends  RuntimeException{
    public ExpiredcodeException(String message) {
        super(message);
    }
}
