package com.cgram.prom.domain.feed.domain.like;


import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.profile.domain.Profile;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(FeedLikeId.class)
public class FeedLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feedId;

    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profileId;

    private boolean isPresent;

    @Builder
    public FeedLike(Feed feedId, Profile profileId, boolean isPresent) {
        this.feedId = feedId;
        this.profileId = profileId;
        this.isPresent = isPresent;
    }

    public void like() {
        this.isPresent = true;
    }

    public void unlike() {
        this.isPresent = false;
    }
}

