package com.cgram.prom.global.security.jwt.service;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.exception.TokenException;
import com.cgram.prom.global.security.jwt.exception.TokenExceptionType;
import com.cgram.prom.global.security.jwt.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken(token)
            .createdAt(LocalDateTime.now())
            .user(user)
            .build();
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new TokenException(
                TokenExceptionType.TOKEN_INVALID));
    }

    public void deleteToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
