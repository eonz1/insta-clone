package com.cgram.prom.domain.statistics.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.repository.StatisticsRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @InjectMocks
    StatisticsServiceImpl service;

    @Mock
    StatisticsRepository statisticsRepository;

    @Test
    @DisplayName("없으면 insert")
    public void updateStatisticsInsert() {
        // given
        Statistics mockStatics = Statistics.builder()
            .counts(3)
            .type(StatisticType.COMMENT.label())
            .uuid(UUID.randomUUID())
            .build();
        given(statisticsRepository.findByUuid(mockStatics.getUuid()))
            .willReturn(Optional.empty());

        // when
        service.updateStatistics(mockStatics.getUuid(), mockStatics.getType(), 1);

        // then
        verify(statisticsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("통계 증가 update")
    public void updateStatisticsIncrease() {
        // given
        Statistics mockStatics = Statistics.builder()
            .counts(3)
            .type(StatisticType.COMMENT.label())
            .uuid(UUID.randomUUID())
            .build();
        given(statisticsRepository.findByUuid(mockStatics.getUuid()))
            .willReturn(Optional.of(mockStatics));

        // when
        service.updateStatistics(mockStatics.getUuid(), mockStatics.getType(), 1);

        // then
        verify(statisticsRepository, never()).save(any());
        Statistics result = statisticsRepository.findByUuid(mockStatics.getUuid()).get();
        assertThat(result.getCounts()).isEqualTo(4);
    }


    @Test
    @DisplayName("통계 감소 update")
    public void updateStatisticsDecrease() {
        // given
        Statistics mockStatics = Statistics.builder()
            .counts(3)
            .type(StatisticType.COMMENT.label())
            .uuid(UUID.randomUUID())
            .build();
        given(statisticsRepository.findByUuid(mockStatics.getUuid()))
            .willReturn(Optional.of(mockStatics));

        // when
        service.updateStatistics(mockStatics.getUuid(), mockStatics.getType(), -1);

        // then
        verify(statisticsRepository, never()).save(any());
        Statistics result = statisticsRepository.findByUuid(mockStatics.getUuid()).get();
        assertThat(result.getCounts()).isEqualTo(2);
    }
}