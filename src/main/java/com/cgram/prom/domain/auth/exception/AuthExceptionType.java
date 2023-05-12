package com.cgram.prom.domain.auth.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthExceptionType implements BaseExceptionType {
    AUTH_FAILED(HttpStatus.BAD_REQUEST, "이메일, 비밀번호를 잘못 입력하였습니다.");

    private final HttpStatus status;
    private final String message;

    AuthExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
