package com.cgram.prom.domain.statistics.service;

import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.repository.StatisticsRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final StatisticsRepository statisticsRepository;

    @Override
    public void updateStatistics(UUID id, String type, int value) {

        Optional<Statistics> optionalStatistics = statisticsRepository.findByUuid(id);

        if(optionalStatistics.isPresent()) {
            optionalStatistics.get().updateCounts(optionalStatistics.get().getCounts() + value);
        } else {
            statisticsRepository.save(Statistics.builder()
                .uuid(id)
                .type(type)
                .counts(1)
                .build());
        }
    }
}
