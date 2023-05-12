package com.cgram.prom.domain.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String email;

    @NotBlank(message = "공백은 입력 불가합니다.")
    private String password;

    @Builder
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
