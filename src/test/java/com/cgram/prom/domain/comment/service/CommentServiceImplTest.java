package com.cgram.prom.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.cgram.prom.domain.comment.exception.CommentException;
import com.cgram.prom.domain.comment.exception.CommentExceptionType;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    void 정상_로그인이_되지않으면_댓글_작성이_불가능하다() {
        CommentServiceDTO mockDTO = CommentServiceDTO.builder()
            .userId(UUID.randomUUID().toString())
            .commentId(UUID.randomUUID().toString())
            .build();

        given(profileRepository.findByUserId(UUID.fromString(mockDTO.getUserId()))).willReturn(
            Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            commentService.reply(mockDTO);
        });

        assertThat(exception.getExceptionType()).isEqualTo(UserExceptionType.USER_UNAUTHORIZED);
    }

    @Test
    void 댓글_작성자가_불일치하면_수정_및_삭제가_불가능하다() {
        CommentServiceDTO mockDTO = CommentServiceDTO.builder()
            .userId(UUID.randomUUID().toString())
            .commentId(UUID.randomUUID().toString())
            .build();

        given(profileRepository.findByUserId(UUID.fromString(mockDTO.getUserId()))).willReturn(
            Optional.empty());

        CommentException exception = assertThrows(CommentException.class, () -> {
            commentService.modify(mockDTO);
        });

        assertThat(exception.getExceptionType()).isEqualTo(CommentExceptionType.NOT_MATCH_USER);
    }

    @Test
    void 피드가_불일치하면_수정_및_삭제가_불가능하다() {
        CommentServiceDTO mockDTO = CommentServiceDTO.builder()
            .userId(UUID.randomUUID().toString())
            .commentId(UUID.randomUUID().toString())
            .build();

        Profile mockProfile = Profile.builder().id(UUID.randomUUID()).build();
        given(profileRepository.findByUserId(UUID.fromString(mockDTO.getUserId()))).willReturn(Optional.of(mockProfile));

        given(commentRepository.findById(UUID.fromString(mockDTO.getCommentId())))
            .willReturn(Optional.empty());

        CommentException exception = assertThrows(CommentException.class, () -> {
            commentService.modify(mockDTO);
        });

        assertThat(exception.getExceptionType()).isEqualTo(CommentExceptionType.NOT_MATCH_FEED);
    }
}