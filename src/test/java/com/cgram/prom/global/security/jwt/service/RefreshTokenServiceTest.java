package com.cgram.prom.global.security.jwt.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.exception.TokenException;
import com.cgram.prom.global.security.jwt.repository.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("refresh token 저장")
    public void saveRefreshToken() throws Exception {
        // given
        RefreshToken savedRefreshToken = RefreshToken.builder().build();
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();
        String token = "token";

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(
            savedRefreshToken);

        // when
        ArgumentCaptor<RefreshToken> refreshTokenCaptor = ArgumentCaptor.forClass(
            RefreshToken.class);
        refreshTokenService.save(user, token);

        // then
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture());
        RefreshToken refreshToken = refreshTokenCaptor.getValue();
        Assertions.assertThat(refreshToken.getRefreshToken()).isEqualTo(token);
        Assertions.assertThat(refreshToken.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("refresh token 조회 - 없음")
    public void getRefreshTokenFailed() throws Exception {
        // given
        Optional<RefreshToken> refreshToken = Optional.empty();
        when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(refreshToken);

        // expected
        assertThrows(TokenException.class, () -> {
            refreshTokenService.getRefreshToken("token");
        });
    }

    @Test
    @DisplayName("refresh token 조회")
    public void getRefreshToken() throws Exception {
        // given
        Optional<RefreshToken> refreshToken = Optional.of(RefreshToken.builder().build());
        when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(refreshToken);

        // when
        RefreshToken token = refreshTokenService.getRefreshToken("token");

        // then
        Assertions.assertThat(token).isNotNull();
    }
}