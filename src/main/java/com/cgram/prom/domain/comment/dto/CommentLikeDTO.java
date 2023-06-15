package com.cgram.prom.domain.comment.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeDTO {

    private UUID commentId;

    private UUID userId;

    @Builder
    public CommentLikeDTO(UUID commentId, UUID userId) {
        this.commentId = commentId;
        this.userId = userId;
    }
}
