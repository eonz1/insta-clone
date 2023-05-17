package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.profile.CustomImage;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final FollowService followService;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    @Override
    public void follow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.follow(followedUser, user);
    }

    @Override
    public void unfollow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.unfollow(followedUser, user);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileServiceDto dto) {
        Optional<Profile> profile = profileRepository.findByUserId(dto.getUserId());
        CustomImage customImage;

        if (dto.getImage() != null) {
            customImage = new CustomImage();
        } else {
            customImage = null;
        }

        profile.ifPresent(x -> x.update(dto, customImage));
    }

    @Override
    public void getFeeds(String id) {

    }
}
