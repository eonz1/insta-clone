package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.feed.domain.hashtag.QHashTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Feed> getAllFeedsByHashTag(String hashTag) {
        return queryFactory
            .selectFrom(QFeed.feed)
            .join(QFeed.feed.hashTags, QHashTag.hashTag)
            .on(QHashTag.hashTag.tag.eq(hashTag))
            .fetch();
    }

    public List<Feed> getAllFeedsByProfiles(List<UUID> profileIds) {
        return null;
    }
}
