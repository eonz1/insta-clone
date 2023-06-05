package com.cgram.prom.domain.comment.domain.like;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeId implements Serializable {

    private UUID profileId;
    private UUID commentId;

    @Builder
    public CommentLikeId(UUID profileId, UUID commentId) {
        this.profileId = profileId;
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
