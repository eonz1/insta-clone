package com.cgram.prom.domain.feed.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FeedDTO {

    private UUID id;

    private UUID userId;
    private String email;
    private UUID profileImageId;
    private String profileImagePath;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private int commentCount;
    private int likesCount;

    @Builder
    public FeedDTO(UUID id, UUID userId, String email, UUID profileImageId, String profileImagePath,
        String content, LocalDateTime createdAt, LocalDateTime modifiedAt, int commentCount,
        int likesCount) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.profileImageId = profileImageId;
        this.profileImagePath = profileImagePath;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.commentCount = commentCount;
        this.likesCount = likesCount;
    }
}
