package com.cgram.prom.domain.image.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class ImageException extends BaseException {

    private final ImageExceptionType imageExceptionType;

    public ImageException(ImageExceptionType imageExceptionType) {
        this.imageExceptionType = imageExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.imageExceptionType;
    }
}
