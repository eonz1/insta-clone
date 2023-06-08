package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.feed.domain.hashtag.QHashTag;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.request.GetFeedsServiceDto;
import com.cgram.prom.domain.following.domain.QFollow;
import com.cgram.prom.domain.statistics.domain.QStatistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.querydsl.core.types.Projections;
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

    public List<FeedDTO> getAllFeedsByHashTag(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.profile.id.as("profileId"), QFeed.feed.content, QFeed.feed.createdAt
                , QFeed.feed.modifiedAt, QFeed.feed.isPresent, comment.counts.as("commentCount"), likes.counts.as("likesCount")
            ))
            .from(QFeed.feed)
            .leftJoin(QFeed.feed.hashTags, QHashTag.hashTag).on(QHashTag.hashTag.tag.eq(dto.getTag()))
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .where( loeFeedId(dto.getCursor()),
                QFeed.feed.createdAt.gt(lastDate),
                QFeed.feed.isPresent.eq(true)
            )
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getLimit() + 1)
            .fetch();
    }

    public List<FeedDTO> getAllFeedsByMyFollowings(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        // 내가 팔로우한 사람들
        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.profile.id.as("profileId"), QFeed.feed.content, QFeed.feed.createdAt
                , QFeed.feed.modifiedAt, QFeed.feed.isPresent, comment.counts.as("commentCount"), likes.counts.as("likesCount")
            ))
            .join(QFollow.follow)
            .on(QFollow.follow.isPresent,
                QFollow.follow.profileId.id.eq(UUID.fromString(dto.getProfileId())),
                QFeed.feed.profile.id.eq(QFollow.follow.followedId.id)
            )
            .leftJoin(QFeed.feed.hashTags, QHashTag.hashTag).on(QHashTag.hashTag.tag.eq(dto.getTag()))
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .where(loeFeedId(dto.getCursor()),
                QFeed.feed.createdAt.gt(lastDate),
                QFeed.feed.isPresent.eq(true)
            )
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getLimit() + 1)
            .fetchJoin()
            .fetch();
    }

    public List<FeedDTO> getAllFeedsByMyProfile(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.profile.id.as("profileId"), QFeed.feed.content, QFeed.feed.createdAt
                , QFeed.feed.modifiedAt, QFeed.feed.isPresent, comment.counts.as("commentCount"), likes.counts.as("likesCount")
            ))
            .where(
                QFeed.feed.isPresent.eq(true),
                QFeed.feed.profile.id.eq(UUID.fromString(dto.getProfileId())),
                loeFeedId(dto.getCursor()),
                QFeed.feed.createdAt.gt(lastDate)
            )
            .leftJoin(QFeed.feed.hashTags, QHashTag.hashTag).on(QHashTag.hashTag.tag.eq(dto.getTag()))
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getLimit() + 1)
            .fetch();
    }

    private BooleanExpression loeFeedId(String feedId) {
        if (feedId == null) {
            return null;
        }

        return QFeed.feed.id.loe(UUID.fromString(feedId));
    }
}
