package com.cgram.prom.global.base.exception.controller;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.response.ErrorResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        Map<String, List<String>> validation = e.getFieldErrors().stream()
            .collect(Collectors.groupingBy(FieldError::getField,
                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("잘못된 요청입니다.")
            .code(400)
            .validation(validation)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseException(BaseException e) {
        int status = e.getExceptionType().getStatus().value();
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(status)
            .message(e.getExceptionType().getMessage())
            .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
