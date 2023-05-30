package com.cgram.prom.domain.comment.like;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeId implements Serializable {

    private UUID userId;
    private UUID commentId;

    @Builder
    public CommentLikeId(UUID userId, UUID commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
