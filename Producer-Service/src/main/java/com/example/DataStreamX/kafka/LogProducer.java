package com.example.DataStreamX.kafka;

import com.example.DataStreamX.Producer.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogProducer {

    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);
    private final LogService logService;

    public LogProducer(LogService logService) {
        this.logService = logService;
    }

    /**
     * Produce a random log at a fixed interval configured in application.yaml (app.logging.interval-ms).
     * Uses fixedRateString so that the interval is configurable without recompiling.
     */
    @Scheduled(fixedRateString = "${app.logging.interval-ms:5000}")
    public void producePeriodicLog() {
        try {
            logService.sendRandomLog();
        } catch (Exception ex) {
            logger.error("Scheduled producePeriodicLog failed: {}", ex.getMessage(), ex);
        }
    }
}