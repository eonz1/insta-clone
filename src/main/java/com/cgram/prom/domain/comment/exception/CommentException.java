package com.cgram.prom.domain.comment.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class CommentException extends BaseException {

    private final CommentExceptionType commentExceptionType;

    public CommentException(CommentExceptionType commentExceptionType) {
        this.commentExceptionType = commentExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return commentExceptionType;
    }
}
