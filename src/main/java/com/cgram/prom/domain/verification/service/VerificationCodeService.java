package com.cgram.prom.domain.verification.service;

import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.domain.verification.domain.VerificationCode;
import com.cgram.prom.domain.verification.exception.VerificationException;
import com.cgram.prom.domain.verification.exception.VerificationExceptionType;
import com.cgram.prom.domain.verification.repository.VerificationCodeRepository;
import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.service.MailSender;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final MailSender ncloudMail;

    @Transactional
    public void sendVerificationCode(String email) {
        if (userRepository.findByEmailAndIsPresent(email, true).isEmpty()) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        VerificationCode verificationCode = verificationCodeRepository.save(VerificationCode.builder()
                                                                                    .email(email)
                                                                                    .code(verificationCodeGenerator.generateCode())
                                                                                    .expirationDate(LocalDateTime.now().plusMinutes(5))
                                                                                    .build());

        MailRequest request = new MailRequest();
        request.setSenderAddress("verification@cgram.com");
        request.setSenderName("cgram");
        request.setTitle("cgram 비밀번호 찾기 인증번호");
        request.setBody("<div>귀하의 인증 코드는 다음과 같습니다: <br>" + verificationCode.getCode()+ "</div>");
        request.addRecipient(verificationCode.getEmail());

        ncloudMail.send(request);
    }

    public VerificationCode get(String email) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findTopByEmailOrderByExpirationDateDesc(email);
        return verificationCode.orElseThrow(() -> new VerificationException(VerificationExceptionType.NOT_FOUND));
    }

    public void matchVerificationCode(String email, String code) {
        validDate(email, LocalDateTime.now());
        validCode(email, code);
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
