package com.cgram.prom.domain.following.service;

import com.cgram.prom.domain.user.domain.User;

public interface FollowService {

    void follow(User followedUser, User user);

    void unfollow(User followedUser, User user);
}
