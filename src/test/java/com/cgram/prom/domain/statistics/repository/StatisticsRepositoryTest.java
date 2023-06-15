package com.cgram.prom.domain.statistics.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("dev")
class StatisticsRepositoryTest {

    @Autowired
    StatisticsRepository repository;

    @Test
    @DisplayName("uuid 로 조회하기")
    void findByUuid() {
        // given
        Statistics statistics = Statistics.builder()
            .uuid(UUID.randomUUID())
            .type(StatisticType.COMMENT.label())
            .counts(12)
            .build();
        repository.save(statistics);

        // when
        Optional<Statistics> optionalStatistics = repository.findByUuid(statistics.getUuid());
        Statistics dbStatistics = optionalStatistics.orElse(null);

        // then
        assertThat(dbStatistics).isNotNull();
        assertThat(dbStatistics.getUuid()).isEqualTo(statistics.getUuid());
        assertThat(dbStatistics.getCounts()).isEqualTo(12);
        assertThat(dbStatistics.getType()).isEqualTo(StatisticType.COMMENT.label());
    }

    @Test
    @DisplayName("uuid와 타입으로 조회하기")
    void findByUuidAndType() {
        // given
        Statistics statistics = Statistics.builder()
            .uuid(UUID.randomUUID())
            .type(StatisticType.COMMENT.label())
            .counts(12)
            .build();
        repository.save(statistics);

        // when
        Optional<Statistics> optionalStatistics = repository.findByUuidAndType(statistics.getUuid(),
            StatisticType.COMMENT.label());
        Statistics dbStatistics = optionalStatistics.orElse(null);

        // then
        assertThat(dbStatistics).isNotNull();
        assertThat(dbStatistics.getUuid()).isEqualTo(statistics.getUuid());
        assertThat(dbStatistics.getCounts()).isEqualTo(12);
        assertThat(dbStatistics.getType()).isEqualTo(StatisticType.COMMENT.label());
    }
}