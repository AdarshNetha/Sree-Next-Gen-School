package com.adarshnetha.SreeNextGenSchool.controller;

import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.exception.TokenExpiredException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseStructure<Object>> handleEntityNotFound(EntityNotFoundException exception) {
        ResponseStructure<Object> responseStructure = new ResponseStructure<>(
                exception.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseStructure);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseStructure<Object>> handleIllegalArgument(IllegalArgumentException exception) {
        ResponseStructure<Object> responseStructure = new ResponseStructure<>(
                exception.getMessage(),
                null,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseStructure);
    }

    @ExceptionHandler({TokenExpiredException.class, MissingRequestHeaderException.class})
    public ResponseEntity<ResponseStructure<Object>> handleTokenExpired() {
        ResponseStructure<Object> responseStructure = new ResponseStructure<>(
                "Token expired",
                null,
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseStructure);
    }
}
