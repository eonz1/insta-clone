package com.cgram.prom.domain.following.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @InjectMocks
    FollowServiceImpl followService;

    @Mock
    FollowRepository followRepository;

    @Test
    @DisplayName("팔로잉 성공")
    public void followUser() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();

        User followedUser = User.builder()
            .id(UUID.randomUUID())
            .build();

        when(followRepository.findById(any(FollowId.class))).thenReturn(
            Optional.empty());

        // when
        followService.follow(followedUser, user);

        // then
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    @DisplayName("이미 팔로잉 기록이 있으면 팔로잉 상태만 활성화시킨다.")
    public void updateFollowingStatus() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();

        User followedUser = User.builder()
            .id(UUID.randomUUID())
            .build();
        Optional<Follow> follow = Optional.of(Follow.builder()
            .userId(user)
            .followedId(followedUser)
            .isPresent(false)
            .build());
        when(followRepository.findById(any(FollowId.class))).thenReturn(follow);

        // when
        followService.follow(followedUser, user);

        // then
        verify(followRepository, never()).save(any(Follow.class));
        Assertions.assertThat(follow.get().isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("언팔로잉")
    public void unfollowUser() throws Exception {
        // given
        User user = User.builder()
            .id(UUID.randomUUID())
            .build();

        User followedUser = User.builder()
            .id(UUID.randomUUID())
            .build();

        Optional<Follow> follow = Optional.of(Follow.builder()
            .userId(user)
            .followedId(followedUser)
            .isPresent(true)
            .build());

        when(followRepository.findById(any(FollowId.class))).thenReturn(
            follow);

        // when
        followService.unfollow(followedUser, user);

        // then
        Assertions.assertThat(follow.get().isPresent()).isEqualTo(false);
    }
}