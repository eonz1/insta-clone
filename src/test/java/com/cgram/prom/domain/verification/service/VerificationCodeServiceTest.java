package com.cgram.prom.domain.verification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.domain.verification.domain.VerificationCode;
import com.cgram.prom.domain.verification.exception.VerificationException;
import com.cgram.prom.domain.verification.exception.VerificationExceptionType;
import com.cgram.prom.domain.verification.repository.VerificationCodeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationCodeGenerator verificationCodeGenerator;

    @InjectMocks
    VerificationCodeService verificationCodeService;

    @Test
    void 탈퇴하거나_존재하지_않는_회원은_코드를_받을_수_없다() {
        given(userRepository.findByEmailAndIsPresent("test@test.com", true))
            .willReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            verificationCodeService.sendVerificationCode("test@test.com");
        });
        assertThat(exception.getExceptionType()).isEqualTo(UserExceptionType.USER_NOT_FOUND);
        assertThat(exception.getExceptionType().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 받은지_5분지나면_인증번호_만료_메세지를_보내야한다() {
        VerificationCode mockCode = VerificationCode.builder()
                                                    .email("test@test.com")
                                                    .code(verificationCodeGenerator.generateCode())
                                                    .expirationDate(LocalDateTime.now().minusMinutes(5))
                                                    .build();
        given(verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(mockCode.getEmail()))
            .willReturn(Optional.of(mockCode));

        VerificationException exception = assertThrows(VerificationException.class, () -> {
            verificationCodeService.validDate(mockCode.getEmail(), LocalDateTime.now());
        });
        assertThat(exception.getExceptionType()).isEqualTo(VerificationExceptionType.EXPIRED);
        assertThat(exception.getExceptionType().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 인증키가_다르면_일치하지_않다는_메세지를_보내야한다() {
        VerificationCode mockCode = VerificationCode.builder()
                                                    .email("test@test.com")
                                                    .code("1s5c36")
                                                    .expirationDate(LocalDateTime.now().plusMinutes(5))
                                                    .build();
        given(verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(mockCode.getEmail()))
            .willReturn(Optional.of(mockCode));

        VerificationException exception = assertThrows(VerificationException.class, () -> {
            verificationCodeService.validCode(mockCode.getEmail(), "1s5c33");
        });
        assertThat(exception.getExceptionType()).isEqualTo(VerificationExceptionType.NOT_CORRECT);
        assertThat(exception.getExceptionType().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 인증키_존재하지_않을때_메세지를_보내야한다() {
        VerificationException exception = assertThrows(VerificationException.class, () -> {
            verificationCodeService.get("test@test.com");
        });

        assertThat(exception.getExceptionType()).isEqualTo(VerificationExceptionType.NOT_FOUND);
        assertThat(exception.getExceptionType().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}