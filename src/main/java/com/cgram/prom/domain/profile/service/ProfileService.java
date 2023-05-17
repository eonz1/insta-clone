package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;

public interface ProfileService {

    void follow(String followedId, String userId);

    void unfollow(String followedId, String userId);

    void updateProfile(UpdateProfileServiceDto dto);

    void getFeeds(String id);
}
