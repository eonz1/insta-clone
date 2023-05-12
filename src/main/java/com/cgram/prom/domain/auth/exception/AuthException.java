package com.cgram.prom.domain.auth.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class AuthException extends BaseException {

    private AuthExceptionType exceptionType;

    public AuthException(AuthExceptionType authExceptionType) {
        this.exceptionType = authExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
