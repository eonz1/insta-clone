package com.cgram.prom.domain.profile.controller;

import com.cgram.prom.domain.profile.request.UpdateProfileRequest;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.profile.response.ProfileResponse;
import com.cgram.prom.domain.profile.service.ProfileService;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String id,
        Authentication authentication) {

        return ResponseEntity.status(200).body(profileService.getProfile(UUID.fromString(id),
            UUID.fromString(authentication.getName())));
    }

    @PatchMapping(value = "/{id}", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public void updateProfile(
        @PathVariable String id,
        Authentication authentication,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request", required = false) UpdateProfileRequest request) {
        if (!authentication.getName().equals(id)) {
            throw new UserException(UserExceptionType.USER_UNAUTHORIZED);
        }

        UpdateProfileServiceDto dto = UpdateProfileServiceDto.builder()
            .userId(UUID.fromString(authentication.getName()))
            .intro(request.getIntro())
            .isPublic(request.getIsPublic())
            .image(image)
            .build();
        profileService.updateProfile(dto);
    }

    @PostMapping("/{id}/following")
    public void follow(Authentication authentication, @PathVariable String id) {
        profileService.follow(UUID.fromString(id), UUID.fromString(authentication.getName()));
    }

    @DeleteMapping("/{id}/following")
    public void unfollow(Authentication authentication, @PathVariable String id) {
        profileService.unfollow(UUID.fromString(id), UUID.fromString(authentication.getName()));
    }

    @GetMapping("/{id}/feeds")
    public void getFeeds(@PathVariable String id) {
        profileService.getFeeds(id);
    }
}
