package br.com.caminhodasaguas.api.configs.exceptions.handler;

import br.com.caminhodasaguas.api.configs.exceptions.ExperienceTourismAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.MunicipalityAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.caminhodasaguas.api.DTO.ExceptionDTO;
import br.com.caminhodasaguas.api.configs.exceptions.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> EmailAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(BannerException.class)
    public ResponseEntity<ExceptionDTO<String>> BannerException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(EmailInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> EmailInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> UserNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(BannerNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> BannerNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(DocumentInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> DocumentInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(DocumentAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> DocumentAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(PhoneInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> PhoneInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(PhoneAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> PhoneAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> EmailNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(ExperienceTourismNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> ExperienceTourismNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(ExperienceTourismAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> ExperienceTourismAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(CodeInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> CodeInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(MaxSizeInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> MaxSizeInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(ExpiredcodeException.class)
    public ResponseEntity<ExceptionDTO<String>> ExpiredcodeException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(MunicipalityAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> MunicipalityAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(MinioHandlerException.class)
    public ResponseEntity<ExceptionDTO<String>> MinioHandlerException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(FlavorsCultureAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDTO<String>> FlavorsCultureAlreadyRegisteredException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(FlavorsCultureNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> FlavorsCultureNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(IntegerInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> IntegerInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }


        @ExceptionHandler(TestimonialsNotFoundException.class)
    public ResponseEntity<ExceptionDTO<String>> TestimonialsNotFoundException(Exception ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ExceptionDTO<String>> TokenInvalidException(Exception ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(
                new ExceptionDTO<String>(status.value(), ex.getMessage(), OffsetDateTime.now()));
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(status).body(
                new ExceptionDTO<Map<String, String>>(status.value(), errors, OffsetDateTime.now()));
    }
}
