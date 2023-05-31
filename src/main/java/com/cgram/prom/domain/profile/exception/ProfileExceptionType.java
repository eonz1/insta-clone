package com.cgram.prom.domain.profile.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProfileExceptionType implements BaseExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "프로필을 찾을 수 없습니다.");

    private HttpStatus status;
    private String message;

    ProfileExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
