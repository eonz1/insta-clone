package com.cgram.prom.domain.auth.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginServiceDto {

    private String email;
    private String password;

    @Builder
    public LoginServiceDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
