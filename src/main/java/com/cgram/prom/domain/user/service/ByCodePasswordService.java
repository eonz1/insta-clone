package com.cgram.prom.domain.user.service;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.domain.verification.service.VerificationCodeService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ByCodePasswordService implements PasswordService {

    private final VerificationCodeService verificationCodeService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void modifyPassword(String email, String password, String code)  {
        verificationCodeService.validDate(email, LocalDateTime.now());
        verificationCodeService.validCode(email, code);

        User user = userRepository.findByEmail(email).get();
        user.updatePassword(passwordEncoder.encode(password));
    }
}
