package com.cgram.prom.global.security.jwt.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TokenExceptionType implements BaseExceptionType {
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    TokenExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
