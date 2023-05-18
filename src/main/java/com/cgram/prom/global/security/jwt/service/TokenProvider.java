package com.cgram.prom.global.security.jwt.service;


import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.domain.Token;
import com.cgram.prom.global.security.jwt.exception.TokenException;
import com.cgram.prom.global.security.jwt.exception.TokenExceptionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final TokenProperties tokenProperties;
    private final RefreshTokenService refreshTokenService;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(tokenProperties.getSecretKey()));
    }

    public Token issueToken(User user) {
        Claims claims = Jwts.claims()
            .setIssuer("cgram.com");
        Claims accessClaims = Jwts.claims().setIssuer("cgram.com")
            .setSubject(user.getId().toString());

        long accessPeriod = 1000L * 60L * 10L;
        long refreshPeriod = 1000L * 60L * 60L * 24L * 7L;

        String accessToken = generateToken(accessClaims, getExpirationDate(accessPeriod));
        String refreshToken = generateToken(claims, getExpirationDate(refreshPeriod));

        refreshTokenService.save(user, refreshToken);

        return new Token(accessToken, refreshToken);
    }

    public Token reIssueToken(String accessToken, String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshToken(refreshToken);

        String userId = refreshTokenEntity.getUser().getId().toString();
        String subject = getTokenSubject(accessToken);

        if (!verifyToken(refreshToken) || !subject.equals(userId)) {
            throw new TokenException(TokenExceptionType.TOKEN_INVALID);
        }
        refreshTokenService.deleteToken(refreshTokenEntity);
        
        return issueToken(refreshTokenEntity.getUser());
    }

    public String generateToken(Claims claims, Date expiredAt) {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(expiredAt)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Date getExpirationDate(long period) {
        Date now = new Date();
        return new Date(now.getTime() + period);
    }

    public String getTokenSubject(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean verifyToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
