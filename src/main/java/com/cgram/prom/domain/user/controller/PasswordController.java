package com.cgram.prom.domain.user.controller;

import com.cgram.prom.domain.user.request.ModifyPasswordRequest;
import com.cgram.prom.domain.user.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final PasswordService byCodePasswordService;

    @PostMapping("/code")
    public void modifyPasswordByCode(@Valid @RequestBody ModifyPasswordRequest request) {
        byCodePasswordService.modifyPassword(request.getEmail(), request.getPassword(), request.getCode());
    }

}
