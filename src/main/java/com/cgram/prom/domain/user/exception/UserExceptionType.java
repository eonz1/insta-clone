package com.cgram.prom.domain.user.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptionType implements BaseExceptionType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    USER_CONFLICT(HttpStatus.CONFLICT, "회원이 이미 존재합니다."),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    UserExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
