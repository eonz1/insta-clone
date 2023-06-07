package com.cgram.prom.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator(style = Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String email;
    private String password;
    private boolean isPresent;

    @Builder
    public User(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isPresent = true;
    }

    public void withdraw() {
        this.isPresent = false;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
