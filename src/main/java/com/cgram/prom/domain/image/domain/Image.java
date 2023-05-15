package com.cgram.prom.domain.image.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String path;

    @ColumnDefault("1")
    private boolean isPresent;

    private LocalDateTime createdAt;

    public void setPresent(boolean present) {
        isPresent = present;
    }

    @Builder
    public Image(UUID id, String path, boolean isPresent, LocalDateTime createdAt) {
        this.id = id;
        this.path = path;
        this.isPresent = isPresent;
        this.createdAt = createdAt;
    }
}
