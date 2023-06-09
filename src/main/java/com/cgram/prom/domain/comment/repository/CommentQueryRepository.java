package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.QComment;
import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.statistics.domain.QStatistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
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

    public List<CommentDTO> getCommentsByFeedIds(List<UUID> feedIds, int limit) {
        return queryFactory
            .select(Projections.fields(CommentDTO.class
                ,QComment.comment.id ,QComment.comment.feed ,QComment.comment.profile
                ,QComment.comment.content ,QComment.comment.createdAt, QComment.comment.modifiedAt
                ,QComment.comment.isPresent, QStatistics.statistics.counts.as("likesCount")
            ))
            .from(QComment.comment)
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
