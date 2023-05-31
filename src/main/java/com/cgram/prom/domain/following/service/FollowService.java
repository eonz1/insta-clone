package com.cgram.prom.domain.following.service;

import com.cgram.prom.domain.profile.domain.Profile;

public interface FollowService {

    void follow(Profile followedUserProfile, Profile userProfile);

    void unfollow(Profile followedUserProfile, Profile userProfile);
}
