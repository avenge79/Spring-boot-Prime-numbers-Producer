package com.rado.producer.scheduler;

import com.rado.producer.service.NumberGenerationService;
import com.rado.producer.service.NumberSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNumbersTaskScheduler {
    private final ThreadPoolTaskScheduler taskScheduler;
    private final NumberGenerationService numberGenerationService;
    private final NumberSendingService numberSendingService;

    @Scheduled(fixedRateString = "${scheduler.milliseconds}")
    public void generateAndSendNumbers() {
        List<Integer> randomNumbers = numberGenerationService.generateNumbers();
        log.info("Generated numbers: {}", randomNumbers);
        if (!numberSendingService.sendNumbers(randomNumbers))
            stopScheduler();
    }

    public void stopScheduler() {
        taskScheduler.shutdown();
        log.info("Scheduler stopped.");
        System.exit(0);
    }
}