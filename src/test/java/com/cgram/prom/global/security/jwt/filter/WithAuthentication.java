package com.cgram.prom.global.security.jwt.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthenticationSecurityContextFactory.class)
public @interface WithAuthentication {

    String name();
}
