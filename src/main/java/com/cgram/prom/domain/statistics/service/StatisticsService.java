package com.cgram.prom.domain.statistics.service;

import java.util.UUID;

public interface StatisticsService {

    void updateStatistics(UUID id, String type, int value);
}
