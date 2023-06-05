package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.profile.domain.Profile;

public interface FeedLikeService {

    void like(Feed feed, Profile profile);

    void unlike(Feed feed, Profile profile);
}
