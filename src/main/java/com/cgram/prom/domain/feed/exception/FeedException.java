package com.cgram.prom.domain.feed.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class FeedException extends BaseException {

    private final FeedExceptionType feedExceptionType;

    public FeedException(FeedExceptionType feedExceptionType) {
        this.feedExceptionType = feedExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.feedExceptionType;
    }
}
