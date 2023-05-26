package com.cgram.prom.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cgram.prom.domain.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    private final String email = "a@gmail.com";

    @Test
    @DisplayName("회원 가입하기")
    public void registerUser() throws Exception {
        // given
        User user = User
            .builder()
            .email(email)
            .password("123123")
            .build();

        // when
        userRepository.save(user);

        // then
        User result = userRepository.findById(user.getId()).get();
        assertThat(result).isEqualTo(user);

    }

    @Test
    @DisplayName("이메일로 회원 찾기")
    public void userFindByEmail() throws Exception {
        // given
        User user = User
            .builder()
            .email(email)
            .password("123123")
            .build();

        userRepository.save(user);

        // when
        User byEmail = userRepository.findByEmail(email).get();

        // then
        assertEquals(email, byEmail.getEmail());
    }

    @Test
    @DisplayName("비밀번호 변경하기")
    public void updatePassword() {
        // given
        userRepository.save(User.builder()
            .email(email)
            .password("123123")
            .build());

        // when
        User beforeUser = userRepository.findByEmail(email).get();
        beforeUser.updatePassword("123456");

        // then
        User afterUser = userRepository.findByEmail(email).get();
        assertThat(afterUser.getPassword()).isEqualTo("123456");
    }

    @Test
    @DisplayName("아이디, 탈퇴여부 조건으로 회원 검색")
    public void findByIdAndIsPresent() throws Exception {
        // given
        User user = userRepository.save(User.builder()
            .id(UUID.randomUUID())
            .build());
        user.withdraw();

        // when
        Optional<User> byIdAndIsPresent = userRepository.findByIdAndIsPresent(
            user.getId(), true);

        // then
        assertTrue(byIdAndIsPresent.isEmpty());
    }
}