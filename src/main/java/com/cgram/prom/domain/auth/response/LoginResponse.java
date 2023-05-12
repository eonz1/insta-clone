package com.cgram.prom.domain.auth.response;

import com.cgram.prom.global.security.jwt.domain.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Token token;

    @Builder
    public LoginResponse(Token token) {
        this.token = token;
    }
}
