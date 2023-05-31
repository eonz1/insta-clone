package com.cgram.prom.domain.following.domain;

import com.cgram.prom.domain.profile.domain.Profile;
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
    private Profile followedId;

    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profileId;

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isPresent;

    @Builder
    public Follow(Profile followedId, Profile profileId, boolean isPresent) {
        this.followedId = followedId;
        this.profileId = profileId;
        this.isPresent = isPresent;
    }

    public void followStatusUpdate(boolean status) {
        this.isPresent = status;
    }

    @Override
    public FollowId getId() {
        return new FollowId(followedId.getId(), profileId.getId());
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
