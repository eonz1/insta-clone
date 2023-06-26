package com.cgram.prom.domain.following.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsServiceImpl;
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

    @Mock
    StatisticsServiceImpl statisticsService;

    @Test
    @DisplayName("팔로잉 성공")
    public void followUser() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        User followedUser = User.builder().id(UUID.randomUUID()).build();

        Profile followedUserProfile = Profile.builder().id(UUID.randomUUID()).user(followedUser)
            .build();
        Profile userProfile = Profile.builder().id(UUID.randomUUID()).user(user).build();

        when(followRepository.findById(any(FollowId.class))).thenReturn(Optional.empty());

        // when
        followService.follow(followedUserProfile, userProfile);

        // then
        verify(followRepository, times(1)).save(any(Follow.class));
        verify(statisticsService, times(1)).updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), 1);
        verify(statisticsService, times(1)).updateStatistics(userProfile.getId(),
            StatisticType.FOLLOWING.label(), 1);
    }

    @Test
    @DisplayName("팔로잉 했다가 취소한 적이 있으면 팔로잉 상태만 활성화시킨다.")
    public void updateFollowingStatus() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        User followedUser = User.builder().id(UUID.randomUUID()).build();

        Profile followedUserProfile = Profile.builder().id(UUID.randomUUID()).user(followedUser)
            .build();
        Profile userProfile = Profile.builder().id(UUID.randomUUID()).user(user).build();

        Optional<Follow> follow = Optional.of(Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(false)
            .build());
        when(followRepository.findById(any(FollowId.class))).thenReturn(follow);

        // when
        followService.follow(followedUserProfile, userProfile);

        // then
        verify(followRepository, never()).save(any(Follow.class));
        Assertions.assertThat(follow.get().isPresent()).isEqualTo(true);
        verify(statisticsService, times(1)).updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), 1);
        verify(statisticsService, times(1)).updateStatistics(userProfile.getId(),
            StatisticType.FOLLOWING.label(), 1);
    }

    @Test
    @DisplayName("언팔로잉")
    public void unfollowUser() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        User followedUser = User.builder().id(UUID.randomUUID()).build();

        Profile followedUserProfile = Profile.builder().id(UUID.randomUUID()).user(followedUser)
            .build();
        Profile userProfile = Profile.builder().id(UUID.randomUUID()).user(user).build();

        Optional<Follow> follow = Optional.of(Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(true)
            .build());

        when(followRepository.findById(any(FollowId.class))).thenReturn(
            follow);

        // when
        followService.unfollow(followedUserProfile, userProfile);

        // then
        Assertions.assertThat(follow.get().isPresent()).isEqualTo(false);
        verify(statisticsService, times(1)).updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), -1);
        verify(statisticsService, times(1)).updateStatistics(userProfile.getId(),
            StatisticType.FOLLOWING.label(), -1);
    }

    @Test
    @DisplayName("이미 팔로잉 되어 있으면 다시 팔로잉 하지 않는다")
    public void followUserDuplicate() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        User followedUser = User.builder().id(UUID.randomUUID()).build();

        Profile followedUserProfile = Profile.builder().id(UUID.randomUUID()).user(followedUser)
            .build();
        Profile userProfile = Profile.builder().id(UUID.randomUUID()).user(user).build();

        Optional<Follow> follow = Optional.of(Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(true)
            .build());
        when(followRepository.findById(any(FollowId.class))).thenReturn(follow);

        // when
        followService.follow(followedUserProfile, userProfile);

        // then
        verify(followRepository, never()).save(any(Follow.class));
        verify(statisticsService, times(0)).updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), 1);
        verify(statisticsService, times(0)).updateStatistics(userProfile.getId(),
            StatisticType.FOLLOWING.label(), 1);
    }

    @Test
    @DisplayName("이미 언팔로잉 되어 있으면 다시 언팔로잉 하지 않는다")
    public void unfollowUserDuplicate() throws Exception {
        // given
        User user = User.builder().id(UUID.randomUUID()).build();
        User followedUser = User.builder().id(UUID.randomUUID()).build();

        Profile followedUserProfile = Profile.builder().id(UUID.randomUUID()).user(followedUser)
            .build();
        Profile userProfile = Profile.builder().id(UUID.randomUUID()).user(user).build();

        Optional<Follow> follow = Optional.of(Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(false)
            .build());

        when(followRepository.findById(any(FollowId.class))).thenReturn(
            follow);

        // when
        followService.unfollow(followedUserProfile, userProfile);

        // then
        verify(statisticsService, times(0)).updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), -1);
        verify(statisticsService, times(0)).updateStatistics(userProfile.getId(),
            StatisticType.FOLLOWING.label(), -1);
    }
}