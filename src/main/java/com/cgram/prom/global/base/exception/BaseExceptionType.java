package com.cgram.prom.global.base.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus getStatus();

    String getMessage();
}
