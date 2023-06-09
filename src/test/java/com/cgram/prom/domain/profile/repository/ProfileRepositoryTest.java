package com.cgram.prom.domain.profile.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.repository.ImageRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository.ProfileWithCounts;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 아이디로 프로필 조회")
    public void getProfileByUserId() throws Exception {
        // given
        User user = userRepository.save(User.builder().build());
        Profile profile = Profile.builder()
            .intro("intro")
            .user(user)
            .build();
        profileRepository.save(profile);

        // when
        Optional<Profile> profileByUserId = profileRepository.findByUserId(user.getId());

        // then
        Assertions.assertThat(profileByUserId.get().getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(profileByUserId.get().isPublic()).isEqualTo(true);
        Assertions.assertThat(profileByUserId.get().getIntro()).isEqualTo("intro");
    }

    @Test
    @Commit
    @DisplayName("회원 아이디로 프로필 조회 +팔로워수 +팔로잉수 +피드수")
    public void getProfileWithCountsByUserId() {
        // given
        User loginUser = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile loginUserProfile = profileRepository.save(
            Profile.builder().user(loginUser).build());

        User followingUser = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile followingUserProfile = profileRepository.save(
            Profile.builder().user(followingUser).build());

        User followingUser2 = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile followingUser2Profile = profileRepository.save(
            Profile.builder().user(followingUser2).build());

        User followerUser = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile followerUserProfile = profileRepository.save(
            Profile.builder().user(followerUser).build());

        User user = userRepository.save(
            User.builder().id(UUID.randomUUID()).email("test@test.com").build());
        Image image = imageRepository.save(
            Image.builder().id(UUID.randomUUID()).path("/test.png").isPresent(true).build());

        Profile userProfile = profileRepository.save(Profile.builder()
            .intro("intro")
            .user(user)
            .image(image)
            .build());
        feedRepository.save(Feed.builder().profile(userProfile).isPresent(true).build());

        // add following
        followRepository.save(Follow.builder()
            .profileId(userProfile)
            .followedId(followingUserProfile)
            .isPresent(true)
            .build());
        followRepository.save(Follow.builder()
            .profileId(userProfile)
            .followedId(followingUser2Profile)
            .isPresent(true)
            .build());
        // add follower
        followRepository.save(Follow.builder()
            .profileId(followerUserProfile)
            .followedId(userProfile)
            .isPresent(true)
            .build());
        // loginUser follow user
        followRepository.save(Follow.builder()
            .profileId(loginUserProfile)
            .followedId(userProfile)
            .isPresent(true)
            .build());

        // when
        ProfileWithCounts profileWithCounts = profileRepository.getProfileWithCountsByProfileId(
            userProfile.getId(), loginUserProfile.getId());

        // then
        Assertions.assertThat(profileWithCounts.getFeedCount()).isEqualTo(1L);
        Assertions.assertThat(profileWithCounts.getFollowingCount()).isEqualTo(2L);
        Assertions.assertThat(profileWithCounts.getFollowerCount()).isEqualTo(2L);
        Assertions.assertThat(profileWithCounts.getIsFollowing()).isEqualTo(1);
    }
}