package com.cgram.prom.domain.feed.response;

import com.cgram.prom.domain.feed.domain.image.FeedImage;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedImageResponse {

    private UUID imageId;
    private Integer imageIndex;

    @Builder
    public FeedImageResponse(FeedImage feedImage) {
        this.imageId = feedImage.getImageId().getId();
        this.imageIndex = feedImage.getImageIndex();
    }
}
