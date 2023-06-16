package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.like.FeedLike;
import com.cgram.prom.domain.feed.domain.like.FeedLikeId;
import com.cgram.prom.domain.feed.exception.FeedLikeException;
import com.cgram.prom.domain.feed.exception.FeedLikeExceptionType;
import com.cgram.prom.domain.feed.repository.FeedLikeRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeServiceImpl implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final StatisticsService statisticsService;

    @Transactional
    @Override
    public void like(Feed feed, Profile profile) {
        FeedLikeId id = FeedLikeId.builder()
            .feedId(feed.getId())
            .profileId(profile.getId())
            .build();
        Optional<FeedLike> feedLike = feedLikeRepository.findById(id);

        if (feedLike.isPresent() && !feedLike.get().isPresent()) {
            feedLike.get().like();

            statisticsService.updateStatistics(feed.getId(), StatisticType.FEED_LIKE.label(), 1);
            return;
        }

        if (feedLike.isEmpty()) {
            FeedLike newFeedLike = FeedLike.builder()
                .feedId(feed)
                .profileId(profile)
                .isPresent(true)
                .build();
            feedLikeRepository.save(newFeedLike);

            statisticsService.updateStatistics(feed.getId(), StatisticType.FEED_LIKE.label(),
                1);
        }
    }

    @Transactional
    @Override
    public void unlike(Feed feed, Profile profile) {
        FeedLikeId id = FeedLikeId.builder()
            .feedId(feed.getId())
            .profileId(profile.getId())
            .build();
        FeedLike feedLike = feedLikeRepository.findById(id)
            .orElseThrow(() -> new FeedLikeException(FeedLikeExceptionType.NOT_FOUND));

        if (!feedLike.isPresent()) {
            return;
        }
        
        feedLike.unlike();

        statisticsService.updateStatistics(feed.getId(), StatisticType.FEED_LIKE.label(), -1);
    }
}
