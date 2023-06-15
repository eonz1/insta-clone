package com.cgram.prom.domain.statistics.repository;

import com.cgram.prom.domain.statistics.domain.Statistics;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Optional<Statistics> findByUuidAndType(UUID uuid, String type);
}
