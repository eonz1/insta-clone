package com.cgram.prom.domain.profile.exception;

import com.cgram.prom.global.base.exception.BaseException;
import com.cgram.prom.global.base.exception.BaseExceptionType;
import lombok.Getter;

@Getter
public class ProfileException extends BaseException {

    private final ProfileExceptionType profileExceptionType;

    public ProfileException(ProfileExceptionType profileExceptionType) {
        this.profileExceptionType = profileExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return profileExceptionType;
    }
}
