package com.cgram.prom.domain.verification.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.domain.verification.domain.VerificationCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VerificationCodeRepositoryTest {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Test
    void 이메일로_만료날짜가_가장_최신인_인증코드_찾기 () {
        // given
        verificationCodeRepository.save(VerificationCode.builder()
                                            .email("test@test.com")
                                            .expirationDate(LocalDateTime.now().minusMinutes(20))
                                            .code("123AA4")
                                            .build());

        verificationCodeRepository.save(VerificationCode.builder()
                                            .email("test@test.com")
                                            .expirationDate(LocalDateTime.now().minusMinutes(10))
                                            .code("13AA4C")
                                            .build());

        verificationCodeRepository.save(VerificationCode.builder()
                                            .email("test@test.com")
                                            .expirationDate(LocalDateTime.now().minusMinutes(5))
                                            .code("3B3AA4")
                                            .build());

        // when
        VerificationCode verificationCode = verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc("test@test.com").get();

        // then
        assertThat(verificationCode.getCode()).isEqualTo("3B3AA4");
    }

}