package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Comment> getAllCommentsByFeedIds(List<UUID> feedIds, int limit) {
        return queryFactory.selectFrom(QComment.comment)
            .where(QComment.comment.feed.id.in(feedIds),
                QComment.comment.isPresent.eq(true)
            )
            .limit(limit)
            .orderBy(QComment.comment.createdAt.desc())
            .fetch();
    }
}
