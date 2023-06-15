package com.cgram.prom.domain.comment.domain.like;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.profile.domain.Profile;
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
    @JoinColumn(name = "profile_id")
    private Profile profileId;

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment commentId;

    private boolean isPresent;

    @Builder
    public CommentLike(Profile profileId, Comment commentId, boolean isPresent) {
        this.profileId = profileId;
        this.commentId = commentId;
        this.isPresent = isPresent;
    }

    public void like() {
        this.isPresent = true;
    }

    public void unlike() {
        this.isPresent = false;
    }
}
