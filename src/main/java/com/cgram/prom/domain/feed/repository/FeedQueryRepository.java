package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.feed.domain.hashtag.QHashTag;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.request.GetFeedsServiceDto;
import com.cgram.prom.domain.following.domain.QFollow;
import com.cgram.prom.domain.image.domain.QImage;
import com.cgram.prom.domain.profile.domain.QProfile;
import com.cgram.prom.domain.statistics.domain.QStatistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.user.domain.QUser;
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

    public List<FeedDTO> getFeedsByHashTag(GetFeedsServiceDto dto) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.content
                , QFeed.feed.createdAt, QFeed.feed.modifiedAt
                , comment.counts.as("commentCount"), likes.counts.as("likesCount")
                , QProfile.profile.user.email, QFeed.feed.profile.user.id.as("userId")
                , QImage.image.path.as("profileImagePath"), QImage.image.id.as("profileImageId")
            ))
            .from(QFeed.feed)
            .where(
                QFeed.feed.isPresent.eq(true),
                loeFeedId(dto.getCursor()),
                eqTag(dto.getTag())
            )
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .leftJoin(QHashTag.hashTag).on(QHashTag.hashTag.feed.id.eq(QFeed.feed.id))
            .join(QProfile.profile).on(QProfile.profile.id.eq(QFeed.feed.profile.id))
            .join(QUser.user).on(QProfile.profile.user.id.eq(QUser.user.id))
            .join(QImage.image).on(QImage.image.id.eq(QProfile.profile.image.id))
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getLimit() + 1)
            .fetch();
    }

    public List<FeedDTO> getFeedsByMyFollowings(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        // 내가 팔로우한 사람들
        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.content
                , QFeed.feed.createdAt, QFeed.feed.modifiedAt
                , comment.counts.as("commentCount"), likes.counts.as("likesCount")
                , QProfile.profile.user.email, QFeed.feed.profile.user.id.as("userId")
                , QImage.image.path.as("profileImagePath"), QImage.image.id.as("profileImageId")
            ))
            .from(QFeed.feed)
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .join(QProfile.profile).on(QProfile.profile.id.eq(QFeed.feed.profile.id))
            .join(QImage.image).on(QImage.image.id.eq(QProfile.profile.image.id))
            .join(QFollow.follow)
            .on(QFollow.follow.isPresent.eq(true),
                QFollow.follow.profileId.id.eq(UUID.fromString(dto.getProfileId())),
                QFollow.follow.followedId.id.eq(QFeed.feed.profile.id)
            )
            .where(loeFeedId(dto.getCursor()),
                QFeed.feed.createdAt.gt(lastDate),
                QFeed.feed.isPresent.eq(true)
            )
            .orderBy(QFeed.feed.createdAt.desc())
            .limit(dto.getLimit() + 1)
            .fetch();
    }

    public List<FeedDTO> getFeedsByUser(GetFeedsServiceDto dto, LocalDateTime lastDate) {
        QStatistics comment = new QStatistics("comment");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(FeedDTO.class
                , QFeed.feed.id, QFeed.feed.content
                , QFeed.feed.createdAt, QFeed.feed.modifiedAt
                , comment.counts.as("commentCount"), likes.counts.as("likesCount")
                , QProfile.profile.user.email, QFeed.feed.profile.user.id.as("userId")
                , QImage.image.path.as("profileImagePath"), QImage.image.id.as("profileImageId")
            ))
            .from(QFeed.feed)
            .where(
                QFeed.feed.isPresent.eq(true),
                QFeed.feed.createdAt.gt(lastDate),
                eqProfileId(dto.getProfileId()),
                loeFeedId(dto.getCursor())
            )
            .leftJoin(comment).on(comment.uuid.eq(QFeed.feed.id), comment.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes).on(likes.uuid.eq(QFeed.feed.id), likes.type.eq(StatisticType.FEED_LIKE.label()))
            .join(QProfile.profile).on(QProfile.profile.id.eq(QFeed.feed.profile.id))
            .join(QUser.user).on(QProfile.profile.user.id.eq(QUser.user.id))
            .join(QImage.image).on(QImage.image.id.eq(QProfile.profile.image.id))
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

    private BooleanExpression eqProfileId(String profileId) {
        if (profileId == null) {
            return null;
        }

        return QFeed.feed.profile.id.eq(UUID.fromString(profileId));
    }

    private BooleanExpression eqTag(String tag) {
        if (tag == null) {
            return null;
        }

        return QHashTag.hashTag.tag.eq(tag);
    }
}
