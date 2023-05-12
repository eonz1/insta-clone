package com.cgram.prom.domain.auth.service;

import com.cgram.prom.domain.auth.request.LoginServiceDto;
import com.cgram.prom.domain.auth.request.LogoutServiceDto;
import com.cgram.prom.domain.auth.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginResponse login(LoginServiceDto dto);

    void logout(LogoutServiceDto dto);
}
