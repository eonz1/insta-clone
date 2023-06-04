package com.cgram.prom.domain.feed.domain.image;

import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImageId implements Serializable {

    private UUID feedId;
    private UUID imageId;

    @Builder
    public FeedImageId(UUID feedId, UUID imageId) {
        this.feedId = feedId;
        this.imageId = imageId;
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
