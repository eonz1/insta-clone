package com.cgram.prom.domain.following.domain;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowId implements Serializable {

    private UUID followedId;
    private UUID profileId;

    public FollowId(UUID followedId, UUID profileId) {
        this.followedId = followedId;
        this.profileId = profileId;
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
