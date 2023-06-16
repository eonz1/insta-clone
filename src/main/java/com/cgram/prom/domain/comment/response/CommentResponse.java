package com.cgram.prom.domain.comment.response;

import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private UUID id;

    private String content;

    private UUID userId;

    private String userEmail;

    private UUID profileImageId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    private long likes;

    @Builder
    public CommentResponse(UUID id, String content, UUID userId, String userEmail,
        UUID profileImageId, LocalDateTime createdAt, LocalDateTime modifiedAt, long likes) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.userEmail = userEmail;
        this.profileImageId = profileImageId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likes = likes;
    }

    public CommentResponse(CommentDTO dto) {
        this.id = dto.getId();
        this.content = dto.getContent();
        this.userId = dto.getUserId();
        this.userEmail = dto.getUserEmail();
        this.createdAt = dto.getCreatedAt();
        this.modifiedAt = dto.getModifiedAt();
        this.likes = dto.getLikesCount();
        this.profileImageId = dto.getProfileImageId();
    }
}
