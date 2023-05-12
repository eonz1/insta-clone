package com.cgram.prom.global.security.jwt.repository;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.global.security.jwt.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("refreshToken 조회하기")
    public void getRefreshTokenByToken() throws Exception {
        // given
        User user = userRepository.save(User.builder().build());
        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .refreshToken("token")
            .createdAt(LocalDateTime.now())
            .build();
        
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> token = refreshTokenRepository.findByRefreshToken("token");

        // then
        Assertions.assertThat(token.get().getRefreshToken()).isEqualTo("token");
    }
}