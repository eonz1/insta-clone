package com.cgram.prom.domain.feed.domain.image;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.image.domain.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@IdClass(FeedImageId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {

    @Id
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feedId;

    @Id
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image imageId;

    private boolean isPresent;
    private boolean isCover;

    @Builder
    public FeedImage(Feed feedId, Image imageId, boolean isPresent, boolean isCover) {
        this.feedId = feedId;
        this.imageId = imageId;
        this.isPresent = isPresent;
        this.isCover = isCover;
    }
}
