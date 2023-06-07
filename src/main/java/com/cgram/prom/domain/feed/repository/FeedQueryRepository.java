package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.feed.domain.hashtag.QHashTag;
import com.cgram.prom.domain.feed.request.GetFeedsServiceDto;
import com.cgram.prom.domain.following.domain.QFollow;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Feed> getAllFeedsByHashTag(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        return queryFactory
            .selectFrom(QFeed.feed)
            .join(QFeed.feed.hashTags, QHashTag.hashTag)
            .on(QHashTag.hashTag.tag.eq(dto.getTag()))
            .where(loeFeedId(dto.getCursor()))
            .on(QFeed.feed.createdAt.gt(lastDate))
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getOffset() + 1)
            .fetch();
    }

    public List<Feed> getAllFeedsByMyFollowings(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        // 내가 팔로우한 사람들
        return queryFactory
            .selectFrom(QFeed.feed)
            .join(QFollow.follow)
            .on(QFollow.follow.isPresent,
                QFollow.follow.profileId.id.eq(UUID.fromString(dto.getProfileId())))
            .on(QFeed.feed.profile.id.eq(QFollow.follow.followedId.id))
            .on(QFeed.feed.createdAt.gt(lastDate))
            .where(loeFeedId(dto.getCursor()))
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getOffset() + 1)
            .fetchJoin()
            .fetch();
    }

    public List<Feed> getAllFeedsByMyProfile(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        return queryFactory
            .selectFrom(QFeed.feed)
            .where(
                QFeed.feed.isPresent,
                QFeed.feed.profile.id.eq(UUID.fromString(dto.getProfileId())),
                loeFeedId(dto.getCursor()),
                QFeed.feed.createdAt.gt(lastDate)
            )
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getOffset() + 1)
            .fetch();
    }

    private BooleanExpression loeFeedId(String feedId) {
        if (feedId == null) {
            return null;
        }

        return QFeed.feed.id.loe(UUID.fromString(feedId));
    }
}
