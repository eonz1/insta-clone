package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final FollowService followService;
    private final UserService userService;

    @Override
    public void follow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.follow(followedUser, user);
    }

    @Override
    public void unfollow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.unfollow(followedUser, user);
    }
}
