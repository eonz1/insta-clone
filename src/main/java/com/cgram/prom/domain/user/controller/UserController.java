package com.cgram.prom.domain.user.controller;

import com.cgram.prom.domain.user.exception.UserException;
import com.cgram.prom.domain.user.exception.UserExceptionType;
import com.cgram.prom.domain.user.request.RegisterServiceDto;
import com.cgram.prom.domain.user.request.RegisterUserRequest;
import com.cgram.prom.domain.user.service.UserService;
import com.cgram.prom.global.security.jwt.filter.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public void registerUser(@Valid @RequestBody RegisterUserRequest request) {
        RegisterServiceDto dto = RegisterServiceDto.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .build();
        userService.register(dto);
    }

    @DeleteMapping("/{id}")
    public void withdrawUser(@PathVariable String id,
        @AuthenticationPrincipal AuthUser user) {

        if (!id.equals(user.getUsername())) {
            throw new UserException(UserExceptionType.USER_UNAUTHORIZED);
        }

        userService.withdrawUser(user.getUsername());
    }
}
