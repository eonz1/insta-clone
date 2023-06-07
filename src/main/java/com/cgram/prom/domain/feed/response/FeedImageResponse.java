package com.cgram.prom.domain.feed.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedImageResponse {

    private UUID imageId;
    private String path;
    private Integer imageIndex;

    @Builder
    public FeedImageResponse(UUID imageId, String path, Integer imageIndex) {
        this.imageId = imageId;
        this.path = path;
        this.imageIndex = imageIndex;
    }
}
