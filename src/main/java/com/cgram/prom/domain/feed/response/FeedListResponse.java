package com.cgram.prom.domain.feed.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedListResponse {

    private List<FeedResponse> feeds;

    private String nextId;
}
