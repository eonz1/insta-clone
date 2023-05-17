package com.cgram.prom.domain.user.service;

import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.infra.mail.model.MailRequest;
import com.cgram.prom.infra.mail.service.MailSender;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailSender ncloudMail;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Transactional
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException(UserExceptionType.USER_CONFLICT);
        }

        User newUser = userRepository.save(user);
        Profile profile = Profile.builder().user(newUser).isPublic(true).build();
        profileRepository.save(profile);
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

    public User loginValidate(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmailAndIsPresent(email, true);

        if (byEmail.isEmpty()) {
            return null;
        }

        boolean matches = passwordEncoder.matches(password, byEmail.get().getPassword());
        if (!matches) {
            return null;
        }

        return byEmail.get();
    }

    public User getUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    @Transactional
    public void withdrawUser(String userId) {
        User user = getUserById(userId);

        user.withdraw();
    }

    public User getUserByIdAndIsPresent(String id) {
        return userRepository.findByIdAndIsPresent(UUID.fromString(id), true)
            .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}
