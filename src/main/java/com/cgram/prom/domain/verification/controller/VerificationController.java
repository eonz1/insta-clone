package com.cgram.prom.domain.verification.controller;

import com.cgram.prom.domain.verification.request.VerificationRequest;
import com.cgram.prom.domain.verification.service.VerificationCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verification")
public class VerificationController {

    private final VerificationCodeService verificationCodeService;

    @PostMapping("")
    public void getVerificationCode(@Valid @RequestBody VerificationRequest verificationRequest) {
        verificationCodeService.sendVerificationCode(verificationRequest.getEmail());
    }


}
