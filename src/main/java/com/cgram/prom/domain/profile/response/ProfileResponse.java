package com.cgram.prom.domain.profile.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String id;
    private String email;
    private String imagePath;
    private String intro;
    private boolean isPublic;
    private Long feedCount;
    private Long followerCount;
    private Long followingCount;
    private boolean isFollowing;

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void updateId(String id) {
        this.id = id;
    }
}
