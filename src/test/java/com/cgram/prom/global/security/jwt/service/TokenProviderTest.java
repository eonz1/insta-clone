package com.cgram.prom.global.security.jwt.service;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.domain.Token;
import com.cgram.prom.global.security.jwt.exception.TokenException;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    JwtDecoder jwtDecoder;

    @MockBean
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("토큰 발급")
    public void issueToken() throws Exception {
        // given
        doNothing().when(refreshTokenService).save(any(User.class), anyString());
        User user = User.builder().id(UUID.randomUUID()).build();

        // when
        Token token = tokenProvider.issueToken(user);

        // then
        Assertions.assertThat(jwtDecoder.decode(token.getAccessToken()).getSubject())
            .isEqualTo(user.getId().toString());
    }

    @Test
    @DisplayName("토큰 재발급 - 실패")
    public void reissueTokenFailed() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();
        String refreshToken = tokenProvider.issueToken(user).getRefreshToken();
        when(refreshTokenService.getRefreshToken(anyString())).thenThrow(TokenException.class);

        // expected
        assertThrows(TokenException.class, () -> {
            tokenProvider.reIssueToken(refreshToken);
        });
    }

    @Test
    @DisplayName("토큰 재발급")
    public void reissueToken() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();

        String refreshToken = "refreshToken";

        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .refreshToken(refreshToken)
            .user(user)
            .build();
        when(refreshTokenService.getRefreshToken(anyString())).thenReturn(refreshTokenEntity);

        // when
        Token newToken = tokenProvider.reIssueToken(refreshToken);

        // then
        assertNotEquals(newToken.getRefreshToken(), refreshToken);
    }
}