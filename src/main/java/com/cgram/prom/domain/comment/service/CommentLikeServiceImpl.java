package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.domain.like.CommentLike;
import com.cgram.prom.domain.comment.domain.like.CommentLikeId;
import com.cgram.prom.domain.comment.dto.CommentLikeDTO;
import com.cgram.prom.domain.comment.exception.CommentLikeException;
import com.cgram.prom.domain.comment.exception.CommentLikeExceptionType;
import com.cgram.prom.domain.comment.repository.CommentLikeRepository;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService{

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final StatisticsService statisticsService;

    @Transactional
    @Override
    public void like(CommentLikeDTO dto) {

        Profile profile = profileRepository.findByUserId(dto.getUserId()).get();
        Comment comment = commentRepository.findById(dto.getCommentId()).get();

        CommentLikeId id = CommentLikeId.builder()
            .commentId(comment.getId())
            .profileId(profile.getId())
            .build();

        Optional<CommentLike> commentLike = commentLikeRepository.findById(id);

        if(commentLike.isPresent() && !commentLike.get().isPresent()) {
            commentLike.get().like();

            statisticsService.updateStatistics(comment.getId(), StatisticType.COMMENT_LIKE.label(), 1);
            return;
        }

        if(commentLike.isEmpty()) {
            CommentLike insertCommentLike = CommentLike.builder()
                .commentId(comment)
                .profileId(profile)
                .isPresent(true)
                .build();
            commentLikeRepository.save(insertCommentLike);

            statisticsService.updateStatistics(comment.getId(), StatisticType.COMMENT_LIKE.label(),
                1);
        }
    }

    @Transactional
    @Override
    public void unlike(CommentLikeDTO dto) {

        Profile profile = profileRepository.findByUserId(dto.getUserId()).get();
        Comment comment = commentRepository.findById(dto.getCommentId()).get();

        CommentLikeId id = CommentLikeId.builder()
            .commentId(comment.getId())
            .profileId(profile.getId())
            .build();

        CommentLike commentLike = commentLikeRepository.findById(id)
            .orElseThrow(() -> new CommentLikeException(CommentLikeExceptionType.NOT_FOUND));

        if(!commentLike.isPresent()) {
            return;
        }

        commentLike.unlike();

        statisticsService.updateStatistics(comment.getId(), StatisticType.COMMENT_LIKE.label(), -1);
    }
}
