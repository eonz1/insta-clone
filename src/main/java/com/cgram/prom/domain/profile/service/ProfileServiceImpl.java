package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.feed.service.FeedQueryService;
import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.service.FileConverter;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.exception.ProfileException;
import com.cgram.prom.domain.profile.exception.ProfileExceptionType;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.profile.repository.ProfileRepository.ProfileWithCounts;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.profile.response.ProfileResponse;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final FollowService followService;
    private final ProfileRepository profileRepository;
    private final ImageService imageService;
    private final FeedQueryService feedQueryService;
    private final FileConverter fileConverter;

    @Override
    public void follow(UUID followedProfileId, UUID userId) {
        Profile followedProfile = profileRepository.findById(followedProfileId)
            .orElseThrow(() -> new ProfileException(
                ProfileExceptionType.NOT_FOUND));
        Profile userProfile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new ProfileException(ProfileExceptionType.NOT_FOUND));

        followService.follow(followedProfile, userProfile);
    }

    @Override
    public void unfollow(UUID followedProfileId, UUID userId) {
        Profile followedProfile = profileRepository.findById(followedProfileId)
            .orElseThrow(() -> new ProfileException(
                ProfileExceptionType.NOT_FOUND));
        Profile userProfile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new ProfileException(ProfileExceptionType.NOT_FOUND));

        followService.unfollow(followedProfile, userProfile);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileServiceDto dto) {
        Optional<Profile> profile = profileRepository.findByUserId(dto.getUserId());
        Image image;
        if (dto.getImage() != null) {
            File imageFile = fileConverter.transferMultipartFileToFile(dto.getImage());
            try {
                image = imageService.saveImage(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 처리에 실패하였습니다.");
            }
        } else {
            image = null;
        }

        profile.ifPresent(x -> x.update(dto, image));
    }

    @Override
    public FeedListResponse getFeeds(GetFeedsDto dto) {
        return feedQueryService.getFeedsByUser(dto);
    }

    @Override
    public ProfileResponse getProfile(UUID userId, UUID loginUserId) {
        Profile userProfile = profileRepository.findByUserId(userId).orElseThrow(
            () -> new UserException(UserExceptionType.USER_NOT_FOUND));
        Profile loginUserProfile = profileRepository.findByUserId(loginUserId).orElseThrow(
            () -> new UserException(UserExceptionType.USER_NOT_FOUND));

        ProfileWithCounts profileDto = profileRepository.getProfileWithCountsByProfileId(
            userProfile.getId(), loginUserProfile.getId());
        boolean isFollowing = (profileDto.getIsFollowing() != 0);

        return ProfileResponse.builder()
            .id(userProfile.getId().toString())
            .email(profileDto.getEmail())
            .imageId(profileDto.getImageId())
            .intro(profileDto.getIntro())
            .isPublic(profileDto.getIsPublic())
            .followerCount(profileDto.getFollowerCount())
            .followingCount(profileDto.getFollowingCount())
            .feedCount(profileDto.getFeedCount())
            .isFollowing(isFollowing)
            .build();
    }
}
