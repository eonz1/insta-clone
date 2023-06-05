package com.cgram.prom.domain.feed.domain.hashtag;

import com.cgram.prom.domain.feed.domain.Feed;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private String tag;
    private boolean isPresent;

    @Builder
    public HashTag(Long id, Feed feed, String tag, Boolean isPresent) {
        this.id = id;
        this.feed = feed;
        this.tag = tag;
        this.isPresent = isPresent;
    }
}
