package com.cgram.prom.user.service;

import com.cgram.prom.user.domain.User;
import com.cgram.prom.user.exception.UserException;
import com.cgram.prom.user.exception.UserExceptionType;
import com.cgram.prom.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException(UserExceptionType.USER_CONFLICT);
        }

        userRepository.save(user);
    }
}
