package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;

public interface FeedService {

    void post(PostFeedServiceDto dto);

    void delete(DeleteFeedServiceDto dto);
}
