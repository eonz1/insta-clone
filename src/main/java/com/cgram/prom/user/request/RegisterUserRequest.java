package com.cgram.prom.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterUserRequest {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Size(max = 50, message = "이메일 최대 길이를 넘겼습니다.")
    @NotBlank(message = "공백은 입력 불가합니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$", message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    @Builder
    public RegisterUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
