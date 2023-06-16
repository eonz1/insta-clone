package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.QComment;
import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.feed.domain.QFeed;
import com.cgram.prom.domain.image.domain.QImage;
import com.cgram.prom.domain.profile.domain.QProfile;
import com.cgram.prom.domain.statistics.domain.QStatistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CommentDTO> getCommentsByFeedIds(List<UUID> feedIds, long limit) {
        return queryFactory
            .select(Projections.fields(CommentDTO.class
                ,QComment.comment.id
                ,QFeed.feed.id.as("feedId")
                ,QComment.comment.profile.user.id.as("userId")
                ,QComment.comment.profile.image.id.as("profileImageId")
                ,QComment.comment.profile.user.email.as("userEmail")
                ,QComment.comment.content ,QComment.comment.createdAt, QComment.comment.modifiedAt
                ,QComment.comment.isPresent, QStatistics.statistics.counts.as("likesCount")
            ))
            .from(QComment.comment)
            .join(QComment.comment.feed, QFeed.feed)
            .join(QComment.comment.profile, QProfile.profile)
            .join(QComment.comment.profile.user, QUser.user)
            .leftJoin(QComment.comment.profile.image, QImage.image)
            .leftJoin(QStatistics.statistics)
                .on(QStatistics.statistics.uuid.eq(QComment.comment.id),
                    QStatistics.statistics.type.eq(StatisticType.COMMENT_LIKE.label()))
            .where(QComment.comment.feed.id.in(feedIds),
                QComment.comment.isPresent.eq(true)
            )
            .limit(limit)
            .orderBy(QComment.comment.createdAt.desc())
            .fetch();
    }
}
