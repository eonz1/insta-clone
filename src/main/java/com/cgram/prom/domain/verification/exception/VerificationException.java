package com.cgram.prom.domain.verification.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class VerificationException extends BaseException {

    private final VerificationExceptionType verificationExceptionType;

    public VerificationException(VerificationExceptionType verificationExceptionType) {
        this.verificationExceptionType = verificationExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return verificationExceptionType;
    }
}
