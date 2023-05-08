package com.cgram.prom.domain.verification.service;

import com.cgram.prom.domain.verification.domain.VerificationCode;
import com.cgram.prom.domain.verification.exception.VerificationException;
import com.cgram.prom.domain.verification.exception.VerificationExceptionType;
import com.cgram.prom.domain.verification.repository.VerificationCodeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;

    public VerificationCode get(String email) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(email);
        return verificationCode.orElseThrow(() -> new VerificationException(VerificationExceptionType.NOT_FOUND));
    }

    public void add(String email) {
        VerificationCode verificationCode = VerificationCode.builder()
                                                            .email(email)
                                                            .code(verificationCodeGenerator.generateCode())
                                                            .expirationDate(LocalDateTime.now().plusMinutes(5))
                                                            .build();

        verificationCodeRepository.save(verificationCode);
    }

    public void validCode(String email, String code) {
        VerificationCode verificationCode = get(email);

        if( !verificationCode.getCode().equals(code) )
            throw new VerificationException(VerificationExceptionType.NOT_CORRECT);
    }

    public void validDate(String email, LocalDateTime localDateTime) {
        VerificationCode verificationCode = get(email);

        if( localDateTime.isAfter(verificationCode.getExpirationDate()) )
            throw new VerificationException(VerificationExceptionType.EXPIRED);
    }
}
