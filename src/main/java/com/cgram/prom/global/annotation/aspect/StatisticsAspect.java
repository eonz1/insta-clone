package com.cgram.prom.global.annotation.aspect;

import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.repository.StatisticsRepository;
import jakarta.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class StatisticsAspect {

    private final StatisticsRepository statisticsRepository;

    @After(value = "@annotation(increaseStatistics)")
    @Transactional
    public void IncreaseStatistics(JoinPoint joinPoint, IncreaseStatistics increaseStatistics)
        throws NoSuchFieldException, IllegalAccessException {

        String type = increaseStatistics.type();
        String id = increaseStatistics.id();
        UUID uuid = getUuid(joinPoint, id);

        Optional<Statistics> statistics = statisticsRepository.findByUuid(uuid);

        if(statistics.isPresent()) {
            statistics.get().updateCounts(statistics.get().getCounts() + 1);
        } else {
            statisticsRepository.save(Statistics.builder()
                .uuid(uuid)
                .type(type)
                .counts(1)
                .build());
        }
    }

    @After(value = "@annotation(decreaseStatistics)")
    @Transactional
    public void DecreaseStatistics(JoinPoint joinPoint, DecreaseStatistics decreaseStatistics)
        throws NoSuchFieldException, IllegalAccessException {

        String id = decreaseStatistics.id();
        UUID uuid = getUuid(joinPoint, id);

        Optional<Statistics> statisticsOptional = statisticsRepository.findByUuid(uuid);
        statisticsOptional.ifPresent(statistics -> {
            statisticsOptional.get().updateCounts(statisticsOptional.get().getCounts() - 1);
        });
    }

    private static UUID getUuid(JoinPoint joinPoint, String id)
        throws NoSuchFieldException, IllegalAccessException {

        Field field = joinPoint.getArgs()[0].getClass().getDeclaredField(id);
        field.setAccessible(true);
        UUID uuid = UUID.fromString(field.get(joinPoint.getArgs()[0]).toString());
        return uuid;
    }

}
