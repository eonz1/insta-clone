package com.cgram.prom.global.security.jwt.model;

import com.cgram.prom.global.security.jwt.domain.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueTokenResponse {

    private Token token;

    @Builder
    public ReissueTokenResponse(Token token) {
        this.token = token;
    }
}
