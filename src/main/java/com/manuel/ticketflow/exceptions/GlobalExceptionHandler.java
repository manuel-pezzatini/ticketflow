package com.manuel.ticketflow.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> gestisciJsonNonValido(
            HttpMessageNotReadableException exception) {

        return ResponseEntity
                .badRequest()
                .body("JSON non valido o valore non riconosciuto");

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> gestisciValidazione(
            MethodArgumentNotValidException exception) {

            Map<String, String> errori = new HashMap<>();

            for(FieldError errore: exception.getBindingResult().getFieldErrors()){
                errori.put(errore.getField(), errore.getDefaultMessage());
            }

            return ResponseEntity
                    .badRequest()
                    .body(errori);
    }
    

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> gestisciIllegalArgument(IllegalArgumentException exception){
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> gestisciResourceNotFound(ResourceNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> gestisciAccessoNegato(ForbiddenException exception){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }
}
