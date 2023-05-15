package com.cgram.prom.domain.following.service;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.user.domain.User;
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
    public void follow(User followedUser, User user) {
        Optional<Follow> byUserIdAndFollowedId = followRepository.findById(
            new FollowId(followedUser.getId(), user.getId()));

        if (byUserIdAndFollowedId.isPresent()) {
            byUserIdAndFollowedId.get().followStatusUpdate(true);
            return;
        }

        Follow follow = Follow.builder()
            .userId(user)
            .followedId(followedUser)
            .isPresent(true)
            .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(User followedUser, User user) {
        Optional<Follow> byUserIdAndFollowedId = followRepository.findById(
            new FollowId(followedUser.getId(), user.getId()));

        byUserIdAndFollowedId.ifPresent(follow -> follow.followStatusUpdate(false));
    }
}
