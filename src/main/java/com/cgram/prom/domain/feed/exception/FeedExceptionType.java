package com.cgram.prom.domain.feed.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeedExceptionType implements BaseExceptionType {
    OVER_MAX_IMAGES(HttpStatus.BAD_REQUEST, "이미지 업로드 개수를 초과하였습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 피드에 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    FeedExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
