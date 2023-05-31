package com.cgram.prom.domain.following.service;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    @Transactional
    public void follow(Profile followedUserProfile, Profile userProfile) {
        Optional<Follow> byId = followRepository.findById(
            new FollowId(followedUserProfile.getId(), userProfile.getId()));

        if (byId.isPresent()) {
            byId.get().followStatusUpdate(true);
            return;
        }

        Follow follow = Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(true)
            .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(Profile followedUserProfile, Profile userProfile) {
        Optional<Follow> byId = followRepository.findById(
            new FollowId(followedUserProfile.getId(), userProfile.getId()));

        byId.ifPresent(follow -> follow.followStatusUpdate(false));
    }
}
