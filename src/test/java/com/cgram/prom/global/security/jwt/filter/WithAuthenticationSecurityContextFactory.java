package com.cgram.prom.global.security.jwt.filter;

import com.cgram.prom.global.config.SecurityConfig;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {SecurityConfig.class})
public class WithAuthenticationSecurityContextFactory implements
    WithSecurityContextFactory<WithAuthentication> {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Override
    public SecurityContext createSecurityContext(WithAuthentication annotation) {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<com.nimbusds.jose.proc.SecurityContext> jwks = new ImmutableJWKSet<>(
            new JWKSet(jwk));
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwks);
        Instant now = Instant.now();

        long expiry = 3600L;

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("cgram.com")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(annotation.name())
            .build();

        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwt,
            new ArrayList<>());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(jwtAuthenticationToken);
        return context;
    }
}
