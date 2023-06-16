package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.domain.image.QFeedImage;
import com.cgram.prom.domain.image.domain.QImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedImageQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<FeedImage> getAllFeedImagesByFeedId(List<UUID> feedIds) {

        return queryFactory
            .selectFrom(QFeedImage.feedImage)
            .join(QFeedImage.feedImage.imageId, QImage.image)
            .on(QImage.image.id.eq(QFeedImage.feedImage.imageId.id))
            .where(QFeedImage.feedImage.feedId.id.in(feedIds),
                QFeedImage.feedImage.isPresent.eq(true))
            .orderBy(QFeedImage.feedImage.imageId.createdAt.asc())
            .fetch();
    }

}
