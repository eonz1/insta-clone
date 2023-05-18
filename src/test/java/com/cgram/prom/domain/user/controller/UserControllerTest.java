package com.cgram.prom.domain.user.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgram.prom.domain.user.request.RegisterServiceDto;
import com.cgram.prom.domain.user.request.RegisterUserRequest;
import com.cgram.prom.domain.user.service.UserService;
import com.cgram.prom.global.security.jwt.filter.WithAuthUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    public void registerTest() throws Exception {
        doNothing().when(userService).register(any(RegisterServiceDto.class));

        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("a@naver.com")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isOk())
            .andDo(print());

        ArgumentCaptor<RegisterServiceDto> registerCaptor = ArgumentCaptor.forClass(
            RegisterServiceDto.class);
        verify(userService).register(registerCaptor.capture());
        RegisterServiceDto dto = registerCaptor.getValue();

        Assertions.assertThat(dto.getEmail()).isEqualTo(request.getEmail());
        Assertions.assertThat(dto.getPassword()).isEqualTo(request.getPassword());
    }

    @Test
    @DisplayName("이메일 공백 유효성 검사")
    @WithMockUser
    public void validateEmailWhiteSpace() throws Exception {
        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("     ")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.email")
                .value(containsInAnyOrder("공백은 입력 불가합니다.", "이메일 형식에 맞지 않습니다.")))
            .andDo(print());
    }

    @Test
    @DisplayName("이메일 최대 길이 유효성 검사 - 공백문자")
    @WithMockUser
    public void validateEmailMaxLengthWithWhiteSpace() throws Exception {
        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("                                                                     ")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.validation.email").value(
                    containsInAnyOrder("이메일 최대 길이를 넘겼습니다.", "공백은 입력 불가합니다.",
                        "이메일 형식에 맞지 않습니다.")))
            .andDo(print());
    }

    @Test
    @DisplayName("이메일 최대 길이 유효성 검사")
    @WithMockUser
    public void validateEmailMaxLength() throws Exception {
        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("abcdefghijklmnopqrstuvwxyz123456789012345@gmail.com")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.validation.email").value(
                    containsInAnyOrder("이메일 최대 길이를 넘겼습니다.")))
            .andDo(print());
    }

    @Test
    @DisplayName("이메일 최대 길이, 형식 유효성 검사")
    @WithMockUser
    public void validateEmailMaxLengthAndFormat() throws Exception {
        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("a111111111111bcdefghijklmnopqrstuvwxyz123456789012345awefw")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.validation.email").value(
                    containsInAnyOrder("이메일 형식에 맞지 않습니다.", "이메일 최대 길이를 넘겼습니다.")))
            .andDo(print());
    }

    @Test
    @DisplayName("이메일 빈 값 유효성 검사")
    @WithMockUser
    public void validateEmailEmptyValue() throws Exception {
        // given
        RegisterUserRequest request = RegisterUserRequest.builder()
            .email("")
            .password("at12t1234#!")
            .build();

        // expected
        mockMvc.perform(post("/api/v1/users")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.validation.email").value(
                    containsInAnyOrder("공백은 입력 불가합니다.")))
            .andDo(print());
    }

    @Test
    @DisplayName("패스워드 유효성 검사")
    @WithMockUser
    public void validatePassword() throws Exception {
        List<String> invalidPasswords = Arrays.asList("a1!", "lengthtest", "notnumber!!",
            "notSpecific123",
            "maxlengthtest12345!!#", "test123,,,");

        for (String password : invalidPasswords) {
            // given
            RegisterUserRequest request = RegisterUserRequest.builder()
                .email("a@email.com")
                .password(password)
                .build();

            // expected
            mockMvc.perform(post("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.validation.password").value(
                        containsInAnyOrder("비밀번호 형식이 맞지 않습니다.")))
                .andDo(print());
        }
    }

    @Test
    @DisplayName("회원 탈퇴 - 인증된 사용자와 path로 받은 사용자 다르면 실패")
    @WithAuthUser(username = "jason", refreshToken = "refresh")
    public void withdrawFailed() throws Exception {
        // given
        mockMvc.perform(delete("/api/v1/users/{id}", "jason1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", "jason")
                .header("Refresh", "refresh")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print());

        // expected
        verify(userService, never()).withdrawUser("jason");
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공")
    @WithAuthUser(username = "jason", refreshToken = "refresh")
    public void withdraw() throws Exception {
        // given
        mockMvc.perform(delete("/api/v1/users/{id}", "jason")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", "jason")
                .header("Refresh", "refresh")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        // expected
        verify(userService, times(1)).withdrawUser("jason");
    }
}