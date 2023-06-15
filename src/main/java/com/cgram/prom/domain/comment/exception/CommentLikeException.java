package com.cgram.prom.domain.comment.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class CommentLikeException extends BaseException {

    private final CommentLikeExceptionType commentLikeExceptionType;

    public CommentLikeException(CommentLikeExceptionType commentLikeExceptionType) {
        this.commentLikeExceptionType = commentLikeExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return commentLikeExceptionType;
    }
}
