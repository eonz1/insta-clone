package com.cgram.prom.domain.user.service;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.service.MailSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailSender ncloudMail;

    @Transactional
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException(UserExceptionType.USER_CONFLICT);
        }

        userRepository.save(user);
        sendWelcomeEmail(user.getEmail());
    }

    private void sendWelcomeEmail(String email) {
        MailRequest request = new MailRequest();
        request.setSenderAddress("welcome@cgram.com");
        request.setSenderName("cgram");
        request.setTitle("가입을 축하합니다.");
        request.setBody("cgram 가입을 축하합니다.<br />");
        request.addRecipient(email);

        ncloudMail.send(request);
    }
}
