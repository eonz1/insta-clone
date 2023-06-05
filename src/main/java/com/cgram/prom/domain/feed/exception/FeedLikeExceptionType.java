package com.cgram.prom.domain.feed.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeedLikeExceptionType implements BaseExceptionType {
    NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 정보가 없습니다.");

    private final HttpStatus status;
    private final String message;

    FeedLikeExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
