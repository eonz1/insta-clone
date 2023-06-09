package com.cgram.prom.domain.auth.controller;

import com.cgram.prom.domain.auth.request.LoginRequest;
import com.cgram.prom.domain.auth.request.LoginServiceDto;
import com.cgram.prom.domain.auth.request.LogoutRequest;
import com.cgram.prom.domain.auth.request.LogoutServiceDto;
import com.cgram.prom.domain.auth.response.LoginResponse;
import com.cgram.prom.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(
            new LoginServiceDto(request.getEmail(), request.getPassword()));

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public void logout(Authentication authentication, @RequestBody LogoutRequest request) {
        LogoutServiceDto dto = new LogoutServiceDto(authentication.getName(),
            request.getRefreshToken());
        authService.logout(dto);
    }
}
