package com.cgram.prom.domain.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {

    private String refreshToken;

    @Builder
    public LogoutRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
