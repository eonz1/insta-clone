package com.cgram.prom.domain.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.exception.ProfileException;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.user.service.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @InjectMocks
    ProfileServiceImpl profileService;
    @Mock
    FollowService followService;
    @Mock
    ProfileRepository profileRepository;
    @Mock
    UserService userService;
    @Mock
    ImageService imageService;

    @Test
    @DisplayName("회원 팔로잉")
    public void followUser() throws Exception {
        // given
        Profile profile = Profile.builder().build();
        Profile userProfile = Profile.builder().build();
        when(profileRepository.findById(any(UUID.class))).thenReturn(Optional.of(profile));
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(userProfile));

        // when
        profileService.follow(UUID.randomUUID(), UUID.randomUUID());

        // then
        verify(followService, times(1)).follow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("회원 팔로잉 - 팔로잉 당하는 회원이 존재하지 않으면 팔로잉 불가")
    public void followFailedFollowedUserNotFound() throws Exception {
        // given
        when(profileRepository.findById(any(UUID.class))).thenThrow(ProfileException.class);

        // expected
        Assertions.assertThrows(ProfileException.class, () -> {
            profileService.follow(UUID.randomUUID(), UUID.randomUUID());
        });
        verify(followService, never()).follow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("회원 팔로잉 - 팔로잉하는 회원이 존재하지 않으면 팔로잉 불가")
    public void followFailedUserNotFound() throws Exception {
        // given
        Profile profile = Profile.builder().build();
        when(profileRepository.findById(any(UUID.class))).thenReturn(Optional.of(profile));
        when(profileRepository.findByUserId(any(UUID.class))).thenThrow(ProfileException.class);

        // expected
        Assertions.assertThrows(ProfileException.class, () -> {
            profileService.follow(UUID.randomUUID(), UUID.randomUUID());
        });
        verify(followService, never()).follow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("회원 언팔로잉")
    public void unfollowUser() throws Exception {
        // given
        Profile profile = Profile.builder().build();
        Profile userProfile = Profile.builder().build();
        when(profileRepository.findById(any(UUID.class))).thenReturn(Optional.of(profile));
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(userProfile));

        // when
        profileService.unfollow(UUID.randomUUID(), UUID.randomUUID());

        // then
        verify(followService, times(1)).unfollow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("회원 언팔로잉 - 언팔로잉 당하는 회원이 존재하지 않으면 언팔로잉 불가")
    public void unfollowFailedFollowedUserNotFound() throws Exception {
        // given
        when(profileRepository.findById(any(UUID.class))).thenThrow(ProfileException.class);

        // expected
        Assertions.assertThrows(ProfileException.class, () -> {
            profileService.unfollow(UUID.randomUUID(), UUID.randomUUID());
        });
        verify(followService, never()).unfollow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("회원 언팔로잉 - 언팔로잉하는 회원이 존재하지 않으면 언팔로잉 불가")
    public void unfollowUserFailed() throws Exception {
        // given
        Profile profile = Profile.builder().build();
        when(profileRepository.findById(any(UUID.class))).thenReturn(Optional.of(profile));
        when(profileRepository.findByUserId(any(UUID.class))).thenThrow(ProfileException.class);

        // expected
        Assertions.assertThrows(ProfileException.class, () -> {
            profileService.unfollow(UUID.randomUUID(), UUID.randomUUID());
        });
        verify(followService, never()).unfollow(any(Profile.class), any(Profile.class));
    }

    @Test
    @DisplayName("프로필 업데이트 - 사진, 인트로만 변경")
    public void updateProfile() throws Exception {
        // given
        UpdateProfileServiceDto dto = UpdateProfileServiceDto.builder().userId(UUID.randomUUID())
            .intro("intro")
            .image(
                new MockMultipartFile("image", "image.png", "image/png",
                    new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/resources/static/images.jpeg")))
            .build();

        Optional<Profile> profile = Optional.of(Profile.builder()
            .intro("소개")
            .build());
        Image image = Image.builder()
            .isPresent(true)
            .id(UUID.randomUUID())
            .build();
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(
            profile);

        when(imageService.saveImage(any(File.class))).thenReturn(image);

        // when
        profileService.updateProfile(dto);

        // then
        assertThat(profile.get().getIntro()).isEqualTo("intro");
        assertThat(profile.get().isPublic()).isEqualTo(true);
        assertThat(profile.get().getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("프로필 업데이트 - 사진만 변경")
    public void updateProfileImage() throws Exception {
        // given
        UpdateProfileServiceDto dto = UpdateProfileServiceDto.builder().userId(UUID.randomUUID())
            .image(
                new MockMultipartFile("image", "image.png", "image/png",
                    new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/resources/static/images.jpeg")))
            .build();

        Optional<Profile> profile = Optional.of(Profile.builder()
            .intro("소개")
            .build());
        Image image = Image.builder()
            .isPresent(true)
            .id(UUID.randomUUID())
            .build();
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(
            profile);

        when(imageService.saveImage(any(File.class))).thenReturn(image);

        // when
        profileService.updateProfile(dto);

        // then
        assertThat(profile.get().getIntro()).isEqualTo("소개");
        assertThat(profile.get().isPublic()).isEqualTo(true);
        assertThat(profile.get().getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("프로필 업데이트 - 인트로, 공개여부만 변경")
    public void updateProfileIntroAndPublic() throws Exception {
        // given
        UpdateProfileServiceDto dto = UpdateProfileServiceDto.builder().userId(UUID.randomUUID())
            .intro("intro")
            .isPublic(false)
            .build();

        Optional<Profile> profile = Optional.of(Profile.builder()
            .intro("소개")
            .build());

        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(
            profile);

        // when
        profileService.updateProfile(dto);

        // then
        assertThat(profile.get().getIntro()).isEqualTo("intro");
        assertThat(profile.get().isPublic()).isEqualTo(false);
        assertThat(profile.get().getImage()).isNull();
    }
}