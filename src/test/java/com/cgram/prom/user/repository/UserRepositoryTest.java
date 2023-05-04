package com.cgram.prom.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cgram.prom.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
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
}