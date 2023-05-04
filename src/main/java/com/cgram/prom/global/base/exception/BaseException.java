package com.cgram.prom.global.base.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    public abstract BaseExceptionType getExceptionType();
}
