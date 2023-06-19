package com.cgram.prom.domain.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentQueryRequest {

    private String nextId;

    private String feedId;

    private long limit;

    @Builder
    public CommentQueryRequest(String nextId, String feedId, long limit) {
        this.nextId = nextId;
        this.feedId = feedId;
        this.limit = limit;
    }
}
