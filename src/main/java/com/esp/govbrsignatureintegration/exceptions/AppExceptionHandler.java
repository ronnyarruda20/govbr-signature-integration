package com.esp.govbrsignatureintegration.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException exception) {
        logger.info("Handling AccessDeniedException: {}", exception.getMessage());
        return new ErrorMessage(new Date(), exception.getMessage(), "Acesso não autorizado", HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleWebClientResponseException(WebClientResponseException ex) {
        logger.info("Handling WebClientResponseException: {}", ex.getStatusText());
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getStatusText());
        errorDetails.put("status", String.valueOf(ex.getRawStatusCode()));
        return ResponseEntity
                .status(ex.getRawStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDetails);
    }

    @ExceptionHandler(ImproperDigitalIdentityLevelException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleImproperDigitalIdentityLevelException(ImproperDigitalIdentityLevelException exception) {
        logger.info("Handling ImproperDigitalIdentityLevelException: {}", exception.getMessage());
        return exception.getErrorMessage();
    }

    @ExceptionHandler(GeneralSecurityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleGeneralSecurityException(GeneralSecurityException exception) {
        logger.info("Handling GeneralSecurityException: {}", exception.getMessage());
        return new ErrorMessage(new Date(), exception.getMessage(), "Erro de segurança", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIOException(IOException exception) {
        logger.info("Handling IOException: {}", exception.getMessage());
        return new ErrorMessage(new Date(), exception.getMessage(), "Erro de entrada/saída", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGenericException(Exception exception) {
        logger.error("Unhandled Exception: {}", exception.getMessage(), exception);
        return new ErrorMessage(new Date(), exception.getMessage(), "Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

