package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.response.FeedListResponse;

public interface FeedQueryService {

    FeedListResponse getFeeds(GetFeedsDto dto);

    FeedListResponse getFeedsByUser(GetFeedsDto dto);
}
