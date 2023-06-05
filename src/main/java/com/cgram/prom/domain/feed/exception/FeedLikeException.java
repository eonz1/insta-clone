package com.cgram.prom.domain.feed.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class FeedLikeException extends BaseException {

    private final FeedLikeExceptionType feedLikeExceptionType;

    public FeedLikeException(FeedLikeExceptionType feedLikeExceptionType) {
        this.feedLikeExceptionType = feedLikeExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return feedLikeExceptionType;
    }
}
