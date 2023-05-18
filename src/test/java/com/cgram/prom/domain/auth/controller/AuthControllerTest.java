package com.cgram.prom.domain.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgram.prom.domain.auth.request.LoginRequest;
import com.cgram.prom.domain.auth.request.LoginServiceDto;
import com.cgram.prom.domain.auth.request.LogoutServiceDto;
import com.cgram.prom.domain.auth.service.AuthService;
import com.cgram.prom.global.security.jwt.filter.WithAuthUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 테스트")
    @WithMockUser
    public void loginTest() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
            .email("a@gmail.com")
            .password("test1234!!!")
            .build();

        // when
        mockMvc.perform(post("/api/v1/auth/login")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andExpect(status().isOk())
            .andDo(print());

        ArgumentCaptor<LoginServiceDto> loginCaptor = ArgumentCaptor.forClass(
            LoginServiceDto.class);
        verify(authService).login(loginCaptor.capture());
        LoginServiceDto dto = loginCaptor.getValue();

        // then
        assertThat(dto.getEmail()).isEqualTo("a@gmail.com");
        assertThat(dto.getPassword()).isEqualTo("test1234!!!");
    }

    @Test
    @DisplayName("로그아웃 테스트")
    @WithAuthUser(username = "6a12dw1qwr-ae12fwg9aw-weg2shlwwq-aw10a2doew", refreshToken = "refresh")
    public void logoutTest() throws Exception {
        // given

        // when
        mockMvc.perform(post("/api/v1/auth/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        ArgumentCaptor<LogoutServiceDto> logoutCaptor = ArgumentCaptor.forClass(
            LogoutServiceDto.class);
        verify(authService).logout(logoutCaptor.capture());
        LogoutServiceDto dto = logoutCaptor.getValue();

        // then
        assertThat(dto.getUserId()).isEqualTo("6a12dw1qwr-ae12fwg9aw-weg2shlwwq-aw10a2doew");
        assertThat(dto.getRefreshToken()).isEqualTo("refresh");
    }
}