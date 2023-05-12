package com.cgram.prom.global.security.jwt.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class TokenException extends BaseException {

    private final TokenExceptionType tokenExceptionType;

    public TokenException(TokenExceptionType tokenExceptionType) {
        this.tokenExceptionType = tokenExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.tokenExceptionType;
    }
}
