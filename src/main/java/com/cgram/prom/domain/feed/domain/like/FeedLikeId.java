package com.cgram.prom.domain.feed.domain.like;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedLikeId implements Serializable {

    private UUID feedId;
    private UUID userId;

    public FeedLikeId(UUID feedId, UUID userId) {
        this.feedId = feedId;
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
