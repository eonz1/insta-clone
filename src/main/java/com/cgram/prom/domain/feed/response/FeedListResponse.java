package com.cgram.prom.domain.feed.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedListResponse {

    private List<FeedResponse> feeds;

    private String nextId;

    @Builder
    public FeedListResponse(List<FeedResponse> feeds, String nextId) {
        this.feeds = feeds;
        this.nextId = nextId;
    }
}
