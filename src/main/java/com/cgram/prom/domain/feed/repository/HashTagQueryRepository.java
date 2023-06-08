package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.hashtag.QHashTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class HashTagQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<HashTag> getAllHashTagsByFeedIds(List<UUID> feedIds) {
        return queryFactory.selectFrom(QHashTag.hashTag)
            .where(QHashTag.hashTag.feed.id.in(feedIds))
            .fetch();
    }
}
