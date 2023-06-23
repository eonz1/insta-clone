package com.cgram.prom.domain.profile.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String id;
    private String email;
    private String imageId;
    private String intro;
    private boolean isPublic;
    private long feedCount;
    private long followerCount;
    private long followingCount;
    private boolean isFollowing;

    public void updateImageId(String imageId) {
        this.imageId = imageId;
    }

    public void updateId(String id) {
        this.id = id;
    }
}
