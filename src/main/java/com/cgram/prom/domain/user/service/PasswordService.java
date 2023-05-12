package com.cgram.prom.domain.user.service;

import com.cgram.prom.domain.user.request.ModifyPasswordRequest;

public interface PasswordService {

    void modifyPassword(String email, String password, String code);

}
