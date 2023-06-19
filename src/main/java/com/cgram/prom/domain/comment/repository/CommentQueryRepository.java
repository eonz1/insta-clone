package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.QComment;
import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.comment.request.CommentQueryRequest;
import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.image.domain.QImage;
import com.cgram.prom.domain.profile.domain.QProfile;
import com.cgram.prom.domain.statistics.domain.QStatistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CommentDTO> findByFeedIdsAndLimit(List<UUID> feedIds, long limit) {
        QStatistics count = new QStatistics("count");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(CommentDTO.class
                ,QComment.comment.id
                ,QFeed.feed.id.as("feedId")
                ,QComment.comment.profile.user.id.as("userId")
                ,QComment.comment.profile.image.id.as("profileImageId")
                ,QComment.comment.profile.user.email.as("userEmail")
                ,QComment.comment.content, QComment.comment.createdAt, QComment.comment.modifiedAt
                ,count.counts.as("totalCount"),likes.counts.as("likesCount")
            ))
            .from(QComment.comment)
            .join(QComment.comment.feed, QFeed.feed)
            .join(QComment.comment.profile, QProfile.profile)
            .join(QComment.comment.profile.user, QUser.user)
            .leftJoin(QComment.comment.profile.image, QImage.image)
            .leftJoin(count)
            .on(count.uuid.eq(QComment.comment.feed.id), count.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes)
            .on(likes.uuid.eq(QComment.comment.id), likes.type.eq(StatisticType.COMMENT_LIKE.label()))
            .where(QComment.comment.feed.id.in(feedIds),
                QComment.comment.isPresent.eq(true)
            )
            .limit(limit)
            .orderBy(QComment.comment.createdAt.desc())
            .fetch();
    }

    public List<CommentDTO> findByFeedIdWithPaging(CommentQueryRequest request) {
        QStatistics count = new QStatistics("count");
        QStatistics likes = new QStatistics("likes");

        return queryFactory
            .select(Projections.fields(CommentDTO.class
                ,QFeed.feed.id.as("feedId")
                ,QComment.comment.profile.user.id.as("userId")
                ,QComment.comment.profile.image.id.as("profileImageId")
                ,QComment.comment.profile.user.email.as("userEmail")
                ,QComment.comment.id, QComment.comment.content
                , QComment.comment.createdAt, QComment.comment.modifiedAt
                , count.counts.as("totalCount"), likes.counts.as("likesCount")
            ))
            .from(QComment.comment)
            .join(QComment.comment.feed)
            .join(QComment.comment.profile, QProfile.profile)
            .join(QComment.comment.profile.user, QUser.user)
            .leftJoin(QComment.comment.profile.image, QImage.image)
            .leftJoin(count)
            .on(count.uuid.eq(QComment.comment.feed.id), count.type.eq(StatisticType.COMMENT.label()))
            .leftJoin(likes)
            .on(likes.uuid.eq(QComment.comment.id), likes.type.eq(StatisticType.COMMENT_LIKE.label()))
            .where(QComment.comment.feed.id.eq(UUID.fromString(request.getFeedId()))
                , QComment.comment.isPresent.eq(true)
                , goeCommentId(request.getNextId())
            )
            .orderBy(QComment.comment.createdAt.desc())
            .limit(request.getLimit() + 1)
            .fetch();
    }

    private BooleanExpression goeCommentId(String nextId) {
        if (nextId == null) {
            return null;
        }

        return QComment.comment.id.goe(UUID.fromString(nextId));
    }
}
