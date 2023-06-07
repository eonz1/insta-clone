package com.cgram.prom.domain.feed.domain;

import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.statistics.domain.Statistics;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Feed {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(length = 2200)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private boolean isPresent;

    @OneToOne
    @JoinColumn(name = "uuid")
    private Statistics statistics;

    @OneToMany(mappedBy = "feed")
    private Set<HashTag> hashTags = new HashSet<>();

    @OneToMany(mappedBy = "feedId")
    private List<FeedImage> images = new ArrayList<>();

    @Builder
    public Feed(UUID id, Profile profile, String content, boolean isPresent) {
        this.id = id;
        this.profile = profile;
        this.content = content;
        this.isPresent = isPresent;
    }

    public void delete() {
        this.isPresent = false;
    }

    public void modify(String content) {
        this.content = content == null ? this.content : content;
    }

    public void addHashTag(HashTag hashTag) {
        hashTags.add(hashTag);
        hashTag.setFeed(this);
    }

    public void addImage(FeedImage feedImage) {
        images.add(feedImage);
        feedImage.setFeed(this);
    }
}
