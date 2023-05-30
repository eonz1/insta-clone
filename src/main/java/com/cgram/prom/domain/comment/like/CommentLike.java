package com.cgram.prom.domain.comment.like;

import com.cgram.prom.domain.comment.Comment;
import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CommentLikeId.class)
public class CommentLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment commentId;

    private boolean isPresent;

    @Builder
    public CommentLike(User userId, Comment commentId, boolean isPresent) {
        this.userId = userId;
        this.commentId = commentId;
        this.isPresent = isPresent;
    }
}
