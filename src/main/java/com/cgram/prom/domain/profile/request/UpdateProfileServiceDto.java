package com.cgram.prom.domain.profile.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class UpdateProfileServiceDto {

    private UUID userId;
    private String intro;
    private Boolean isPublic;
    private MultipartFile image;

    @Builder
    public UpdateProfileServiceDto(UUID userId, String intro, Boolean isPublic,
        MultipartFile image) {
        this.userId = userId;
        this.intro = intro;
        this.isPublic = isPublic;
        this.image = image;
    }
}
