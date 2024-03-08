package com.akobir.cache.exception;

import com.akobir.cache.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> errorBody = new HashMap<>();

        for (FieldError fieldError : ex.getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            errorBody.compute(field, (s, strings) -> {
                strings = Objects.requireNonNullElse(strings, new ArrayList<>());
                strings.add(message);
                return strings;
            });
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorBody, request.getRequestURI());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, Object errorBody, String errorPath) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .errorPath(errorPath)
                .errorCode(status.value())
                .errorBody(errorBody)
                .build();
        return new ResponseEntity<>(errorResponseDTO, status);
    }
}
