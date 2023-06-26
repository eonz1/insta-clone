package com.cgram.prom.domain.auth.response;

import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.global.security.jwt.domain.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Token token;

    private String profileId;

    private String userId;

    @Builder
    public LoginResponse(Token token, String profileId, String userId) {
        this.token = token;
        this.profileId = profileId;
        this.userId = userId;
    }
}
