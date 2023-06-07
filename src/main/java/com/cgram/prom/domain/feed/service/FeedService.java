package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;
import com.cgram.prom.domain.feed.response.FeedResponse;
import java.util.UUID;

public interface FeedService {

    void post(PostFeedServiceDto dto);

    void delete(DeleteFeedServiceDto dto);

    void modify(ModifyFeedServiceDto dto);

    FeedResponse getFeed(UUID feedId);
}
