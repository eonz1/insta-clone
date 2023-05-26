package com.cgram.prom.domain.following.domain;

import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(FollowId.class)
@EntityListeners(AuditingEntityListener.class)
public class Follow implements Persistable<FollowId> {

    @Id
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followedId;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isPresent;

    @Builder
    public Follow(User followedId, User userId, boolean isPresent) {
        this.followedId = followedId;
        this.userId = userId;
        this.isPresent = isPresent;
    }

    public void followStatusUpdate(boolean status) {
        this.isPresent = status;
    }

    @Override
    public FollowId getId() {
        return new FollowId(followedId.getId(), userId.getId());
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
