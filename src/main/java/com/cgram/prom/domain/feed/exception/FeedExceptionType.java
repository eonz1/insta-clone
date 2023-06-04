package com.cgram.prom.domain.feed.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeedExceptionType implements BaseExceptionType {
    OVER_MAX_IMAGES(HttpStatus.BAD_REQUEST, "이미지 업로드 개수를 초과하였습니다.");

    private HttpStatus status;
    private String message;

    FeedExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
