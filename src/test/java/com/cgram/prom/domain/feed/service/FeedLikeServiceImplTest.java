package com.cgram.prom.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.like.FeedLike;
import com.cgram.prom.domain.feed.domain.like.FeedLikeId;
import com.cgram.prom.domain.feed.exception.FeedLikeException;
import com.cgram.prom.domain.feed.exception.FeedLikeExceptionType;
import com.cgram.prom.domain.feed.repository.FeedLikeRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedLikeServiceImplTest {

    @InjectMocks
    FeedLikeServiceImpl feedLikeService;

    @Mock
    FeedLikeRepository feedLikeRepository;

    @Test
    @DisplayName("좋아요 이력 있으면 좋아요 상태만 바꿔줌")
    public void feedLikeExist() throws Exception {
        // given
        FeedLike feedLike = FeedLike.builder().isPresent(false).build();
        when(feedLikeRepository.findById(any(FeedLikeId.class))).thenReturn(
            Optional.of(feedLike));

        // when
        feedLikeService.like(Feed.builder().build(), Profile.builder().build());

        // then
        verify(feedLikeRepository, times(0)).save(any(FeedLike.class));
        assertThat(feedLike.isPresent()).isTrue();
    }

    @Test
    @DisplayName("좋아요 이력 없으면 좋아요 엔티티 저장")
    public void feedLikeFirst() throws Exception {
        // given
        when(feedLikeRepository.findById(any(FeedLikeId.class))).thenReturn(
            Optional.empty());

        // when
        feedLikeService.like(Feed.builder().build(), Profile.builder().build());

        // then
        verify(feedLikeRepository, times(1)).save(any(FeedLike.class));
    }

    @Test
    @DisplayName("좋아요 취소 불가능")
    public void feedUnlikeNotFound() throws Exception {
        // given
        when(feedLikeRepository.findById(any(FeedLikeId.class))).thenReturn(
            Optional.empty());

        // when
        FeedLikeException feedLikeException = assertThrows(FeedLikeException.class, () -> {
            feedLikeService.unlike(Feed.builder().build(), Profile.builder().build());
        });

        // then
        assertThat(feedLikeException.getExceptionType()).isEqualTo(FeedLikeExceptionType.NOT_FOUND);
    }

    @Test
    @DisplayName("좋아요 취소")
    public void feedUnlike() throws Exception {
        // given
        FeedLike feedLike = FeedLike.builder().isPresent(true).build();
        when(feedLikeRepository.findById(any(FeedLikeId.class))).thenReturn(
            Optional.of(feedLike));

        // when
        feedLikeService.unlike(Feed.builder().build(), Profile.builder().build());

        // then
        assertThat(feedLike.isPresent()).isFalse();
    }
}