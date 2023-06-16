package com.cgram.prom.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDTO {

    private UUID id;

    private UUID feedId;

    private UUID userId;

    private UUID profileImageId;

    private String userEmail;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private boolean isPresent;

    private int likesCount;

    @Builder
    public CommentDTO(UUID id, UUID feedId, UUID userId, UUID profileImageId,
        String userEmail, String content, LocalDateTime createdAt, LocalDateTime modifiedAt,
        boolean isPresent, int likesCount) {
        this.id = id;
        this.feedId = feedId;
        this.userId = userId;
        this.profileImageId = profileImageId;
        this.userEmail = userEmail;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isPresent = isPresent;
        this.likesCount = likesCount;
    }
}
