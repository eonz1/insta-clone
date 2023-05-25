package com.cgram.prom.global.security.jwt.service;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomSubjectValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("invalid_token", "The required subject is missing", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        if (jwt.getSubject() == null) {
            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}
