package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.exception.CommentException;
import com.cgram.prom.domain.comment.exception.CommentExceptionType;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.comment.response.CommentResponse;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;

    @Override
    public List<CommentResponse> getComments(String feedId) {

        List<Comment> comments = commentRepository.findByFeedIdAndIsPresent(
            UUID.fromString(feedId), true);

        List<CommentResponse> responseList = comments.stream().map(comment ->
            CommentResponse.builder()
                .id(comment.getId())
                .profileId(comment.getProfile().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt()) // TODO: likes
                .build())
            .collect(Collectors.toList());

        return responseList;
    }

    @Override
    @Transactional
    public void reply(CommentServiceDTO dto) {
        Profile profile = profileRepository.findByUserId(UUID.fromString(dto.getUserId()))
            .orElseThrow(() -> new UserException(UserExceptionType.USER_UNAUTHORIZED));

        Comment comment = Comment.builder()
            .feed(Feed.builder().id(UUID.fromString(dto.getFeedId())).build())
            .profile(profile)
            .content(dto.getContent())
            .build();

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void modify(CommentServiceDTO dto) {
        Profile reqProfile = profileRepository.findByUserId(UUID.fromString(dto.getUserId()))
            .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_MATCH_USER));
        Comment comment = commentRepository.findById(UUID.fromString(dto.getCommentId()))
            .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_MATCH_FEED));

        if (!comment.getProfile().getId().equals(reqProfile.getId())) {
            throw new CommentException(CommentExceptionType.NOT_MATCH_USER);
        }

        if (!comment.getFeed().getId().equals(UUID.fromString(dto.getFeedId()))) {
            throw new CommentException(CommentExceptionType.NOT_MATCH_FEED);
        }

        comment.updateContent(dto.getContent());
    }

    @Override
    @Transactional
    public void delete(CommentServiceDTO dto) {
        Profile reqProfile = profileRepository.findByUserId(UUID.fromString(dto.getUserId()))
            .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_MATCH_USER));
        Comment comment = commentRepository.findById(UUID.fromString(dto.getCommentId()))
            .orElseThrow(() -> new CommentException(CommentExceptionType.NOT_MATCH_FEED));

        if (!comment.getProfile().getId().equals(reqProfile.getId())) {
            throw new CommentException(CommentExceptionType.NOT_MATCH_USER);
        }

        if (!comment.getFeed().getId().equals(UUID.fromString(dto.getFeedId()))) {
            throw new CommentException(CommentExceptionType.NOT_MATCH_FEED);
        }

        comment.updateStatus(false);
    }
}
