package com.cgram.prom.global.security.jwt.service;


import static org.junit.jupiter.api.Assertions.assertFalse;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {TokenProvider.class, TokenProperties.class})
@ActiveProfiles(profiles = "dev")
@EnableConfigurationProperties(value = TokenProperties.class)
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    TokenProperties tokenProperties;

    @MockBean
    RefreshTokenService refreshTokenService;

    private SecretKey key;

    @BeforeEach
    void setup() {
        key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(tokenProperties.getSecretKey()));
    }

    @Test
    @DisplayName("토큰 발급")
    public void issueToken() throws Exception {
        // given
        doNothing().when(refreshTokenService).save(any(User.class), anyString());
        User user = User.builder().id(UUID.randomUUID()).build();

        // when
        Token token = tokenProvider.issueToken(user);

        // then
        Assertions.assertThat(tokenProvider.getTokenSubject(token.getAccessToken()))
            .isEqualTo(user.getId().toString());
    }


    @Test
    @DisplayName("토큰 subject 파싱")
    public void parsingTokenSubject() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        Token token = tokenProvider.issueToken(user);

        // when
        String subject = tokenProvider.getTokenSubject(token.getAccessToken());

        // then
        Assertions.assertThat(user.getId().toString()).isEqualTo(subject);
    }


    @Test
    @DisplayName("토큰 유효성 검증 ")
    public void verifyTokenExpired() throws Exception {
        // given
        Date now = new Date();
        String expiredToken = Jwts.builder()
            .setExpiration(new Date(now.getTime() - 30000))
            .signWith(key)
            .compact();

        // when
        boolean result = tokenProvider.verifyToken(expiredToken);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("토큰 유효성 검증 - 형식 맞지 않는 토큰")
    public void verifyTokenInvalid() throws Exception {
        // given
        Date now = new Date();
        String expiredToken = Jwts.builder()
            .setExpiration(new Date(now.getTime() + 30000))
            .signWith(key)
            .compact();
        String[] split = expiredToken.split("\\.");

        // when
        boolean result = tokenProvider.verifyToken(split[0] + split[1]);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("토큰 재발급 - 실패")
    public void reissueTokenFailed() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();
        Date now = new Date();

        String expiredToken = Jwts.builder()
            .setSubject(UUID.randomUUID().toString())
            .setExpiration(new Date(now.getTime() - 30000))
            .signWith(key)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now.getTime() + 300000))
            .signWith(key)
            .compact();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .refreshToken(refreshToken)
            .user(user)
            .build();

        when(refreshTokenService.getRefreshToken(anyString())).thenReturn(refreshTokenEntity);

        // expected
        assertThrows(TokenException.class, () -> {
            tokenProvider.reIssueToken(expiredToken, refreshToken);
        });
    }

    @Test
    @DisplayName("토큰 재발급")
    public void reissueToken() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();
        Date now = new Date();

        String expiredToken = Jwts.builder()
            .setSubject(user.getId().toString())
            .setExpiration(new Date(now.getTime() - 30000))
            .signWith(key)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now.getTime() + 300000))
            .signWith(key)
            .compact();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .refreshToken(refreshToken)
            .user(user)
            .build();
        when(refreshTokenService.getRefreshToken(anyString())).thenReturn(refreshTokenEntity);

        // when
        Token token = tokenProvider.reIssueToken(expiredToken, refreshToken);

        // then
        assertNotEquals(token.getAccessToken(), expiredToken);
        assertNotEquals(token.getRefreshToken(), refreshToken);
    }
}