package com.cgram.prom.domain.profile.service;

public interface ProfileService {

    void follow(String followedId, String userId);

    void unfollow(String followedId, String userId);
}
