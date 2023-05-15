package com.cgram.prom.domain.profile.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @InjectMocks
    ProfileServiceImpl profileService;
    @Mock
    FollowService followService;

    @Mock
    UserService userService;

    @Test
    @DisplayName("회원 팔로잉")
    public void followUser() throws Exception {
        // given
        User user = new User();
        when(userService.getUserByIdAndIsPresent(anyString())).thenReturn(user);

        // when
        profileService.follow(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        // then
        verify(followService, times(1)).follow(any(User.class), any(User.class));
    }

    @Test
    @DisplayName("회원 팔로잉 - 팔로잉 당하는 회원, 팔로잉하는 회원이 둘 다 존재하지 않거나 탈퇴한 회원이면 팔로잉 불가")
    public void followUserFailed() throws Exception {
        // given
        when(userService.getUserByIdAndIsPresent(anyString())).thenThrow(UserException.class);

        // expected
        Assertions.assertThrows(UserException.class, () -> {
            profileService.follow(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        });
        verify(followService, never()).follow(any(User.class), any(User.class));
    }

    @Test
    @DisplayName("회원 언팔로잉")
    public void unfollowUser() throws Exception {
        // given
        User user = new User();
        when(userService.getUserByIdAndIsPresent(anyString())).thenReturn(user);

        // when
        profileService.unfollow(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        // then
        verify(followService, times(1)).unfollow(any(User.class), any(User.class));
    }

    @Test
    @DisplayName("회원 언팔로잉 - 언팔로잉 당하는 회원, 언팔로잉하는 회원이 둘 다 존재하지 않거나 탈퇴한 회원이면 언팔로잉 불가")
    public void unfollowUserFailed() throws Exception {
        // given
        when(userService.getUserByIdAndIsPresent(anyString())).thenThrow(UserException.class);

        // expected
        Assertions.assertThrows(UserException.class, () -> {
            profileService.unfollow(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        });
        verify(followService, never()).unfollow(any(User.class), any(User.class));
    }
}