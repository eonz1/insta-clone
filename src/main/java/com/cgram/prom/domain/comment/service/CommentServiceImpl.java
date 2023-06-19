package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.comment.exception.CommentException;
import com.cgram.prom.domain.comment.exception.CommentExceptionType;
import com.cgram.prom.domain.comment.repository.CommentQueryRepository;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.comment.request.CommentQueryRequest;
import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.comment.response.CommentResponse;
import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final ProfileRepository profileRepository;
    private final StatisticsService statisticsService;

    @Override
    public CommentWithCountResponse getComments(CommentQueryRequest request) {

        List<CommentDTO> comments = commentQueryRepository.findByFeedIdWithPaging(request);
        CommentWithCountResponse response = convertCommentsResponse(comments,
            (int) request.getLimit());

        return response;
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

        statisticsService.updateStatistics(UUID.fromString(dto.getFeedId()), StatisticType.COMMENT.label(), 1);
    }

    @Override
    @Transactional
    public void modify(CommentServiceDTO dto) {

        Comment comment = getComment(dto);
        comment.updateContent(dto.getContent());
    }

    @Override
    @Transactional
    public void delete(CommentServiceDTO dto) {

        Comment comment = getComment(dto);
        comment.updateStatus(false);

        statisticsService.updateStatistics(UUID.fromString(dto.getFeedId()), StatisticType.COMMENT.label(), -1);
    }

    private Comment getComment(CommentServiceDTO dto) {

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

        return comment;
    }

    public CommentWithCountResponse convertCommentsResponse(List<CommentDTO> dtos, int limit) {
        if (dtos == null) {
            return CommentWithCountResponse.builder().totalCount(0).build();
        }

        String nextId = null;

        // nextId 저장, comment 리스트에서 nextData 제거
        if (hasNext(dtos.size(), limit)) {
            int lastFeedIndex = dtos.size() - 1;
            nextId = dtos.get(lastFeedIndex).getId().toString();
            dtos.remove(lastFeedIndex);
        }

        return CommentWithCountResponse.builder()
            .totalCount(dtos.get(0).getTotalCount())
            .comments(dtos.stream().map(CommentResponse::new).toList())
            .nextId(nextId != null ? nextId : "")
            .build();
    }

    public boolean hasNext(int feedSize, int limit) {
        return feedSize == (limit + 1);
    }
}
