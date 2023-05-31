package com.cgram.prom.domain.following.repository;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
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
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FollowRepositoryTest {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("팔로잉 복합키 데이터 조회")
    public void getFollowingByFollowId() throws Exception {
        // given
        User user = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile userProfile = profileRepository.save(Profile.builder().user(user).build());

        User followedUser = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        Profile followedUserProfile = profileRepository.save(
            Profile.builder().user(followedUser).build());

        // when
        Follow follow = Follow.builder()
            .profileId(userProfile)
            .followedId(followedUserProfile)
            .isPresent(true)
            .build();
        followRepository.save(follow);

        // then
        Optional<Follow> byId = followRepository.findById(
            new FollowId(followedUserProfile.getId(), userProfile.getId()));

        Assertions.assertThat(byId.get().getProfileId().getId()).isEqualTo(userProfile.getId());
        Assertions.assertThat(byId.get().getFollowedId().getId())
            .isEqualTo(followedUserProfile.getId());
    }
}