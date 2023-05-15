package com.cgram.prom.domain.following.repository;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
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
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("팔로잉 복합키 데이터 조회")
    public void getFollowingByFollowId() throws Exception {
        // given
        User user = userRepository.save(User.builder().id(UUID.randomUUID()).build());
        User followedUser = userRepository.save(User.builder().id(UUID.randomUUID()).build());

        // when
        Follow follow = Follow.builder()
            .userId(user)
            .followedId(followedUser)
            .isPresent(true)
            .build();
        followRepository.save(follow);

        // then
        Optional<Follow> byId = followRepository.findById(
            new FollowId(followedUser.getId(), user.getId()));

        Assertions.assertThat(byId.get().getUserId().getId()).isEqualTo(user.getId());
        Assertions.assertThat(byId.get().getFollowedId().getId()).isEqualTo(followedUser.getId());
    }
}