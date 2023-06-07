package com.cgram.prom.domain.image.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Image {

    @Id
    @GeneratedValue
    @UuidGenerator(style = Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String path;
    private boolean isPresent;

    @CreatedDate
    private LocalDateTime createdAt;

    public void setPresent(boolean present) {
        isPresent = present;
    }

    @Builder
    public Image(UUID id, String path, boolean isPresent) {
        this.id = id;
        this.path = path;
        this.isPresent = isPresent;
    }
}
