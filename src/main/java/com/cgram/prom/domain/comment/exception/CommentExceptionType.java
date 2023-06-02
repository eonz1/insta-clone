package com.cgram.prom.domain.comment.exception;

import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentExceptionType implements BaseExceptionType {
    NOT_MATCH_USER(HttpStatus.FORBIDDEN, "해당 댓글에 권한이 없습니다."),
    NOT_MATCH_FEED(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;

    CommentExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
