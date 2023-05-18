package com.cgram.prom.domain.profile.controller;

import com.cgram.prom.domain.profile.request.UpdateProfileRequest;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.profile.service.ProfileService;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.global.security.jwt.filter.AuthUser;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PatchMapping(value = "/{id}", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public void updateProfile(
        @PathVariable String id,
        @AuthenticationPrincipal AuthUser user,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request", required = false) UpdateProfileRequest request) {
        if (!user.getUsername().equals(id)) {
            throw new UserException(UserExceptionType.USER_UNAUTHORIZED);
        }

        UpdateProfileServiceDto dto = UpdateProfileServiceDto.builder()
            .userId(UUID.fromString(user.getUsername()))
            .intro(request.getIntro())
            .isPublic(request.getIsPublic())
            .image(image)
            .build();
        profileService.updateProfile(dto);
    }

    @PostMapping("/{id}/following")
    public void follow(@AuthenticationPrincipal AuthUser user, @PathVariable String id) {
        profileService.follow(id, user.getUsername());
    }

    @DeleteMapping("/{id}/following")
    public void unfollow(@AuthenticationPrincipal AuthUser user, @PathVariable String id) {
        profileService.unfollow(id, user.getUsername());
    }

    @GetMapping("/{id}/feeds")
    public void getFeeds(@PathVariable String id) {
        profileService.getFeeds(id);
    }
}
