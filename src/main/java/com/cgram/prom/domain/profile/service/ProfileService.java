package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.profile.response.ProfileResponse;
import java.util.UUID;

public interface ProfileService {

    void follow(UUID followedProfileId, UUID userId);

    void unfollow(UUID followedProfileId, UUID userId);

    void updateProfile(UpdateProfileServiceDto dto);

    FeedListResponse getFeeds(GetFeedsDto id);

    ProfileResponse getProfile(UUID userId, UUID loginProfileId);
}
