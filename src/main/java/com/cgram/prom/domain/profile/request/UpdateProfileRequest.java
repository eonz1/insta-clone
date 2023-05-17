package com.cgram.prom.domain.profile.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {

    private String intro;

    private Boolean isPublic;

    public UpdateProfileRequest(String intro, Boolean isPublic) {
        this.intro = intro;
        this.isPublic = isPublic;
    }
}
