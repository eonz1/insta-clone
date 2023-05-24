package com.cgram.prom.global.security.jwt.exception;

import com.cgram.prom.global.base.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        InvalidBearerTokenException invalidBearerTokenException = new InvalidBearerTokenException(
            authException.getMessage());
        OAuth2Error error = invalidBearerTokenException.getError();

        Map<String, List<String>> validation = new HashMap<>();
        List<String> description = new ArrayList<>(
            Collections.singletonList(error.getDescription()));
        validation.put(error.getErrorCode(), description);

//        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(HttpStatus.UNAUTHORIZED.value())
            .message(TokenExceptionType.TOKEN_INVALID.getMessage())
            .validation(validation)
            .build();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }
}
