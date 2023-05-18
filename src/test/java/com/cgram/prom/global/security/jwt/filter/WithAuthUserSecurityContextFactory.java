package com.cgram.prom.global.security.jwt.filter;

import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAuthUserSecurityContextFactory implements
    WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        AuthUser authUser = AuthUser.builder()
            .userId(annotation.username())
            .refreshToken(annotation.refreshToken())
            .build();
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(authUser, "", new ArrayList<>());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
