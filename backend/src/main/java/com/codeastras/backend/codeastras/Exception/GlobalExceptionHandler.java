package com.codeastras.backend.codeastras.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex){
        return new ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        // quick debug-friendly payload; replace with structured error in prod
        return ResponseEntity.status(500).body(Map.of("error", "internal_server_error"));
    }
}
