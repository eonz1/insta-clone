package com.cgram.prom.domain.feed.domain.like;


import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@IdClass(FeedLikeId.class)
public class FeedLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feedId;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private boolean isPresent;

    public FeedLike(Feed feedId, User userId, boolean isPresent) {
        this.feedId = feedId;
        this.userId = userId;
        this.isPresent = isPresent;
    }
}

