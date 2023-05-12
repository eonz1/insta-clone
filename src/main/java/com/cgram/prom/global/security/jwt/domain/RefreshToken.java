package com.cgram.prom.global.security.jwt.domain;

import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;
    private LocalDateTime createdAt;

    @Builder
    public RefreshToken(Long id, User user, String refreshToken, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
    }
}
