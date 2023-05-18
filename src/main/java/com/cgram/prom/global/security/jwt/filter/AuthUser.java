package com.cgram.prom.global.security.jwt.filter;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
public class AuthUser implements UserDetails {

    private String userId;
    private String refreshToken;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    public AuthUser(String userId, String refreshToken,
        Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.authorities = authorities;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
