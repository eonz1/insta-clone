package com.cgram.prom.domain.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyPasswordRequest {

    @NotBlank(message = "공백은 입력 불가합니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$", message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    private String code;

    @Builder
    public ModifyPasswordRequest(String email, String code, String password) {
        this.email = email;
        this.code = code;
        this.password = password;
    }
}
