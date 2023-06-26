package com.cgram.prom.domain.following.service;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final StatisticsService statisticsService;

    @Override
    @Transactional
    public void follow(Profile followedUserProfile, Profile userProfile) {
        Optional<Follow> follow = followRepository.findById(
            new FollowId(followedUserProfile.getId(), userProfile.getId()));

        if (follow.isPresent()) {
            if (follow.get().isPresent()) {
                return;
            }

            follow.get().followStatusUpdate(true);
            statisticsService.updateStatistics(followedUserProfile.getId(),
                StatisticType.FOLLOWER.label(), 1);
            statisticsService.updateStatistics(userProfile.getId(), StatisticType.FOLLOWING.label(),
                1);
            return;
        }

        Follow newFollow = Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(true)
            .build();

        followRepository.save(newFollow);

        statisticsService.updateStatistics(followedUserProfile.getId(),
            StatisticType.FOLLOWER.label(), 1);
        statisticsService.updateStatistics(userProfile.getId(), StatisticType.FOLLOWING.label(), 1);
    }

    @Override
    @Transactional
    public void unfollow(Profile followedUserProfile, Profile userProfile) {
        Optional<Follow> follow = followRepository.findById(
            new FollowId(followedUserProfile.getId(), userProfile.getId()));

        if (follow.isPresent()) {
            if (!follow.get().isPresent()) {
                return;
            }
            follow.get().followStatusUpdate(false);

            statisticsService.updateStatistics(followedUserProfile.getId(),
                StatisticType.FOLLOWER.label(), -1);
            statisticsService.updateStatistics(userProfile.getId(),
                StatisticType.FOLLOWING.label(),
                -1);
        }
    }
}
