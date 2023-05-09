package com.cgram.prom.domain.verification.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum VerificationExceptionType implements BaseExceptionType {
    EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료 되었습니다."),
    NOT_CORRECT(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "인증번호가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    VerificationExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
