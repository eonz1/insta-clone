package com.cgram.prom.domain.verification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.verification.domain.VerificationCode;
import com.cgram.prom.domain.verification.exception.VerificationException;
import com.cgram.prom.domain.verification.exception.VerificationExceptionType;
import com.cgram.prom.domain.verification.repository.VerificationCodeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@Slf4j
@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private VerificationCodeGenerator verificationCodeGenerator;

    @InjectMocks
    VerificationCodeService verificationCodeService;

    @Test
    void 받은지_5분지나면_만료되어_불가() {
        // given
        VerificationCode mockCode = VerificationCode.builder()
                                                    .email("test@test.com")
                                                    .code(verificationCodeGenerator.generateCode())
                                                    .expirationDate(LocalDateTime.now().minusMinutes(5))
                                                    .build();
        verificationCodeRepository.save(mockCode);
        when(verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(mockCode.getEmail())).thenReturn(
            Optional.of(mockCode));

        // when, then
        VerificationException exception = assertThrows(VerificationException.class, () -> {
            verificationCodeService.validDate(mockCode.getEmail(), LocalDateTime.now());
        });
        assertEquals(exception.getExceptionType(), VerificationExceptionType.EXPIRED);
        assertEquals(exception.getExceptionType().getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void 인증키가_다르면_불가() {
        // given
        VerificationCode mockCode = VerificationCode.builder()
                                                    .email("test2@test.com")
                                                    .code("1s5c36")
                                                    .expirationDate(LocalDateTime.now().plusMinutes(5))
                                                    .build();
        verificationCodeRepository.save(mockCode);
        when(verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(mockCode.getEmail())).thenReturn(
            Optional.of(mockCode));

        // when, then
        VerificationException exception = assertThrows(VerificationException.class, () -> {
            verificationCodeService.validCode(mockCode.getEmail(), "1s5c33");
        });
        assertEquals(exception.getExceptionType(), VerificationExceptionType.NOT_CORRECT);
        assertEquals(exception.getExceptionType().getStatus(), HttpStatus.BAD_REQUEST);
    }
}