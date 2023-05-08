package com.cgram.prom.user.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;

public class UserException extends BaseException {

    private final UserExceptionType userExceptionType;

    public UserException(UserExceptionType userExceptionType) {
        this.userExceptionType = userExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return userExceptionType;
    }
}
