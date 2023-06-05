package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.like.FeedLike;
import com.cgram.prom.domain.feed.domain.like.FeedLikeId;
import com.cgram.prom.domain.feed.exception.FeedLikeException;
import com.cgram.prom.domain.feed.exception.FeedLikeExceptionType;
import com.cgram.prom.domain.feed.repository.FeedLikeRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeServiceImpl implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;

    @Transactional
    @Override
    public void like(Feed feed, Profile profile) {
        FeedLikeId id = FeedLikeId.builder()
            .feedId(feed.getId())
            .profileId(profile.getId())
            .build();
        Optional<FeedLike> byId = feedLikeRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().like();
            return;
        }

        FeedLike feedLike = FeedLike.builder()
            .feedId(feed)
            .profileId(profile)
            .isPresent(true)
            .build();
        feedLikeRepository.save(feedLike);
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

        feedLike.unlike();
    }
}
