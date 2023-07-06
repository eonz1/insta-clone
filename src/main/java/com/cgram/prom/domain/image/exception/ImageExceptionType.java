package com.cgram.prom.domain.image.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ImageExceptionType implements BaseExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ImageExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
