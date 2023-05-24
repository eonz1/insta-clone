package com.cgram.prom.global.security.jwt.service;


import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.domain.Token;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RefreshTokenService refreshTokenService;
    private final JwtEncoder jwtEncoder;

    public Token issueToken(User user) {
        long accessPeriod = 60L * 10L;
        long refreshPeriod = 60L * 60L * 24L * 7L;

        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
            .subject(user.getId().toString())
            .build();

        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
            .subject("")
            .build();

        String accessToken = generateToken(accessClaims, accessPeriod);
        String refreshToken = generateToken(refreshClaims, refreshPeriod);

        refreshTokenService.save(user, refreshToken);

        return new Token(accessToken, refreshToken);
    }

    private String generateToken(JwtClaimsSet claims, long expiredAt) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.from(claims)
            .issuer("cgram.com")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiredAt))

            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public Token reIssueToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshToken(refreshToken);
        refreshTokenService.deleteToken(refreshTokenEntity);

        return issueToken(refreshTokenEntity.getUser());
    }
}
