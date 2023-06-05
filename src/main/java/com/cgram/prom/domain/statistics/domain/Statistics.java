package com.cgram.prom.domain.statistics.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private UUID uuid;

    private int counts;

    @Builder
    public Statistics(String type, UUID uuid, int counts) {
        this.type = type;
        this.uuid = uuid;
        this.counts = counts;
    }

    public void updateCounts(int counts) {
        this.counts = counts;
    }
}
