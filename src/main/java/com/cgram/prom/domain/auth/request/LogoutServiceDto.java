package com.cgram.prom.domain.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutServiceDto {

    private String userId;
    private String refreshToken;

    public LogoutServiceDto(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
