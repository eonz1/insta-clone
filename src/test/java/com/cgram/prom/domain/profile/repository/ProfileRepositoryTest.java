package com.cgram.prom.domain.profile.repository;

import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.repository.UserRepository;
import java.util.Optional;
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
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 아이디로 프로필 조회")
    public void getProfileByUserId() throws Exception {
        // given
        User user = userRepository.save(User.builder().build());
        Profile profile = Profile.builder()
            .intro("intro")
            .isPublic(true)
            .user(user)
            .build();
        profileRepository.save(profile);

        // when
        Optional<Profile> profileByUserId = profileRepository.findByUserId(user.getId());

        // then
        Assertions.assertThat(profileByUserId.get().getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(profileByUserId.get().getIntro()).isEqualTo("intro");
    }
}