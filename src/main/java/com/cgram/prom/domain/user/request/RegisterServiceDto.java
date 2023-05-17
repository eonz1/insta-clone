package com.cgram.prom.domain.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterServiceDto {

    private String email;
    private String password;

    @Builder
    public RegisterServiceDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
