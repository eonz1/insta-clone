package com.cgram.prom.domain.auth.service;

import com.cgram.prom.domain.auth.exception.AuthException;
import com.cgram.prom.domain.auth.exception.AuthExceptionType;
import com.cgram.prom.domain.auth.request.LoginServiceDto;
import com.cgram.prom.domain.auth.request.LogoutServiceDto;
import com.cgram.prom.domain.auth.response.LoginResponse;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.service.UserService;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.service.RefreshTokenService;
import com.cgram.prom.global.security.jwt.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ProfileRepository profileRepository;

    @Override
    public LoginResponse login(LoginServiceDto dto) {
        // 로그인 유효성 검사
        User user = userService.loginValidate(dto.getEmail(), dto.getPassword());
        if (user == null) {
            throw new AuthException(AuthExceptionType.AUTH_FAILED);
        }

        Profile profile = profileRepository.findByUserId(user.getId()).get();

        return LoginResponse.builder()
            .token(tokenProvider.issueToken(user))
            .profileId(profile.getId().toString())
            .userId(user.getId().toString())
            .build();
    }

    @Override
    public void logout(LogoutServiceDto dto) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(dto.getRefreshToken());

        if (dto.getUserId().equals(refreshToken.getUser().getId().toString())) {
            refreshTokenService.deleteToken(refreshToken);
        }
    }
}
