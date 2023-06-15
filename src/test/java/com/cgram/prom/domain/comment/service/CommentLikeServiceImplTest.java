package com.cgram.prom.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.domain.like.CommentLike;
import com.cgram.prom.domain.comment.domain.like.CommentLikeId;
import com.cgram.prom.domain.comment.dto.CommentLikeDTO;
import com.cgram.prom.domain.comment.repository.CommentLikeRepository;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.service.StatisticsServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceImplTest {

    @InjectMocks
    CommentLikeServiceImpl commentLikeService;

    @Mock
    CommentLikeRepository commentLikeRepository;

    @Mock
    StatisticsServiceImpl statisticsService;

    @Mock
    ProfileRepository profileRepository;

    @Mock
    CommentRepository commentRepository;

    @Test
    @DisplayName("좋아요 이력 있으면 상태만 업데이트")
    public void commentLikeExist() {
        // given
        CommentLike commentLike = CommentLike.builder().isPresent(false).build();
        given(commentLikeRepository.findById(any(CommentLikeId.class))).willReturn(
            Optional.of(commentLike));
        given(profileRepository.findByUserId(any())).willReturn(Optional.of(Profile.builder().build()));
        given(commentRepository.findById(any())).willReturn(Optional.of(Comment.builder().build()));

        // when
        commentLikeService.like(
            CommentLikeDTO.builder()
                .commentId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build());

        // then
        assertThat(commentLike.isPresent()).isTrue();
    }

    @Test
    @DisplayName("좋아요 상태 없으면 insert")
    public void commentLikeInsert() {
        // given
        given(profileRepository.findByUserId(any())).willReturn(Optional.of(Profile.builder().build()));
        given(commentRepository.findById(any())).willReturn(Optional.of(Comment.builder().build()));
        given(commentLikeRepository.findById(any(CommentLikeId.class))).willReturn(
            Optional.empty());

        // when
        commentLikeService.like(
            CommentLikeDTO.builder()
                .commentId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build());

        // then
        verify(commentLikeRepository, times(1)).save(any(CommentLike.class));
    }

    @Test
    @DisplayName("좋아요 최소면 상태만 업데이트")
    public void commentUnlike() {
        // given
        CommentLike commentLike = CommentLike.builder().isPresent(true).build();
        given(commentLikeRepository.findById(any(CommentLikeId.class))).willReturn(
            Optional.of(commentLike));
        given(profileRepository.findByUserId(any())).willReturn(Optional.of(Profile.builder().build()));
        given(commentRepository.findById(any())).willReturn(Optional.of(Comment.builder().build()));

        // when
        commentLikeService.unlike(
            CommentLikeDTO.builder()
                .commentId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build());

        // then
        assertThat(commentLike.isPresent()).isFalse();
    }
}