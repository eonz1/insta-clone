package com.cgram.prom.domain.auth.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.auth.exception.AuthException;
import com.cgram.prom.domain.auth.request.LoginServiceDto;
import com.cgram.prom.domain.auth.request.LogoutServiceDto;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.service.UserService;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import com.cgram.prom.global.security.jwt.domain.Token;
import com.cgram.prom.global.security.jwt.service.RefreshTokenService;
import com.cgram.prom.global.security.jwt.service.TokenProvider;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    UserService userService;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenService refreshTokenService;

    @Mock
    ProfileRepository profileRepository;

    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception {
        // given
        LoginServiceDto loginServiceDto = LoginServiceDto.builder()
            .email("a@gmail.com")
            .password("test1234@@@")
            .build();
        User user = User.builder().id(UUID.randomUUID()).build();
        Token token = new Token();
        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        when(userService.loginValidate(anyString(), anyString())).thenReturn(user);
        when(tokenProvider.issueToken(any(User.class))).thenReturn(token);
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(profile));

        // when
        authService.login(loginServiceDto);

        // then
        verify(tokenProvider, times(1)).issueToken(any(User.class));
        verify(profileRepository, times(1)).findByUserId(any(UUID.class));
    }

    @Test
    @DisplayName("로그인 실패")
    public void loginFailed() throws Exception {
        // given
        LoginServiceDto loginServiceDto = LoginServiceDto.builder()
            .email("a@gmail.com")
            .password("test1234@@@")
            .build();
        when(userService.loginValidate(anyString(), anyString())).thenReturn(null);

        // expected
        assertThrows(AuthException.class, () -> {
            authService.login(loginServiceDto);
        });
        verify(tokenProvider, times(0)).issueToken(any(User.class));
    }

    @Test
    @DisplayName("로그아웃 테스트 - 리프레시 토큰 소유자와 권한 userid 다름")
    public void logoutFailed() throws Exception {
        // given
        LogoutServiceDto dto = new LogoutServiceDto("user", "refresh");
        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken("refresh")
            .user(User.builder().id(UUID.randomUUID()).build())
            .build();
        when(refreshTokenService.getRefreshToken("refresh")).thenReturn(refreshToken);

        // when
        authService.logout(dto);

        // then
        verify(refreshTokenService, never()).deleteToken(refreshToken);
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logout() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        LogoutServiceDto dto = new LogoutServiceDto(id, "refresh");
        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken("refresh")
            .user(User.builder().id(UUID.fromString(id)).build())
            .build();
        when(refreshTokenService.getRefreshToken("refresh")).thenReturn(refreshToken);

        // when
        authService.logout(dto);

        // then
        verify(refreshTokenService, times(1)).deleteToken(refreshToken);
    }
}