package com.cgram.prom.global.security.jwt.filter;

import com.cgram.prom.global.security.jwt.domain.Token;
import com.cgram.prom.global.security.jwt.exception.TokenException;
import com.cgram.prom.global.security.jwt.exception.TokenExceptionType;
import com.cgram.prom.global.security.jwt.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") != null || request.getHeader("Refresh") != null) {
            String accessToken = request.getHeader("Authorization");
            String refreshToken = request.getHeader("Refresh");

            // 토큰 검증
            Token token = verifyToken(accessToken, refreshToken);

            // 아이디 추출
            String userId = tokenProvider.getTokenSubject(token.getAccessToken());

            // 인증 객체 만들어서 등록하기
            Authentication authentication = getAuthentication(userId, token.getRefreshToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 재발급한 경우 response header에 넣어주기
            if (!accessToken.equals(token.getAccessToken()) && !refreshToken.equals(
                token.getRefreshToken()) && !request.getServletPath().contains("logout")) {
                response.setHeader("Authorization", token.getAccessToken());
                response.setHeader("Refresh", token.getRefreshToken());
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String userId, String refreshToken) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        UserDetails principal = AuthUser.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .authorities(authorities)
            .build();

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Token verifyToken(String accessToken, String refreshToken) {
        if (accessToken == null) {
            throw new TokenException(TokenExceptionType.TOKEN_INVALID);
        }

        if (tokenProvider.verifyToken(accessToken)) {
            return new Token(accessToken, refreshToken);
        }

        if (refreshToken == null) {
            throw new TokenException(TokenExceptionType.TOKEN_INVALID);
        }

        return tokenProvider.reIssueToken(accessToken, refreshToken);
    }
}
