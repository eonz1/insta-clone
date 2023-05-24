package com.cgram.prom.global.security.jwt.controller;


import com.cgram.prom.global.security.jwt.model.ReissueTokenRequest;
import com.cgram.prom.global.security.jwt.model.ReissueTokenResponse;
import com.cgram.prom.global.security.jwt.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenProvider tokenProvider;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueTokenResponse> reissueToken(
        @RequestBody ReissueTokenRequest request) {
        ReissueTokenResponse response = ReissueTokenResponse.builder()
            .token(tokenProvider.reIssueToken(request.getRefreshToken()))
            .build();

        return ResponseEntity.status(200).body(response);
    }
}
