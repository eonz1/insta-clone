package com.cgram.prom.domain.user.controller;

import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.request.RegisterUserRequest;
import com.cgram.prom.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("")
    public void registerUser(@Valid @RequestBody RegisterUserRequest request) {
        User newUser = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .isPresent(true)
            .build();
        userService.register(newUser);
    }
}
