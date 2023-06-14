package com.cgram.prom.domain.feed.response;

import com.cgram.prom.domain.feed.domain.image.FeedImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedImageResponse {

    private String imagePath;
    private Integer imageIndex;

    @Builder
    public FeedImageResponse(FeedImage feedImage) {
        this.imagePath = feedImage.getImageId().getImageFullPath();
        this.imageIndex = feedImage.getImageIndex();
    }
}
