package com.cgram.prom.domain.statistics.service;

import com.cgram.prom.domain.statistics.domain.Statistics;
import java.util.UUID;

public interface StatisticsService {

    void updateStatistics(UUID id, String type, int value);

    Statistics getStatistics(UUID id, String type);
}
