package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.dto.FeedLikeServiceDto;
import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;

public interface FeedCommandService {

    void post(PostFeedServiceDto dto);

    void delete(DeleteFeedServiceDto dto);

    void modify(ModifyFeedServiceDto dto);

    void like(FeedLikeServiceDto dto);

    void unlike(FeedLikeServiceDto dto);
}
