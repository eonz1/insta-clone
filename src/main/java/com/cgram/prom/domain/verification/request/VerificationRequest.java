package com.cgram.prom.domain.verification.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationRequest {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Size(max = 50, message = "이메일 최대 길이를 넘겼습니다.")
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String email;

    private String code;
}
