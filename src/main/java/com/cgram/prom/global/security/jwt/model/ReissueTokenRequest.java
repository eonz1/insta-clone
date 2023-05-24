package com.cgram.prom.global.security.jwt.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueTokenRequest {

    private String refreshToken;

    @Builder
    public ReissueTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
