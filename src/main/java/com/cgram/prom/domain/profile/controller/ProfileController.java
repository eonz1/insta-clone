package com.cgram.prom.domain.profile.controller;

import com.cgram.prom.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/{id}/following")
    public void follow(@AuthenticationPrincipal User user, @PathVariable String id) {
        profileService.follow(id, user.getUsername());
    }

    @DeleteMapping("/{id}/following")
    public void unfollow(@AuthenticationPrincipal User user, @PathVariable String id) {
        profileService.unfollow(id, user.getUsername());
    }
}
