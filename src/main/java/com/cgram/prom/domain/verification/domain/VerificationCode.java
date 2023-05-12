package com.cgram.prom.domain.verification.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;
    private LocalDateTime expirationDate;

    @Builder
    public VerificationCode(Long id, String email, String code, LocalDateTime expirationDate) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.expirationDate = expirationDate;
    }
}
