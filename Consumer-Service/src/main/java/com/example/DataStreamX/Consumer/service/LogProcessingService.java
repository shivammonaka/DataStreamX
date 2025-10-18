package com.example.DataStreamX.Consumer.service;

import com.example.DataStreamX.Consumer.model.LogEvent;
import com.example.DataStreamX.Consumer.repository.LogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class LogProcessingService {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LogProcessingService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void processBatch(String batchJson) {
        try {
            // Parse the JSON array into a list of LogEvent
            List<LogEvent> logs = objectMapper.readValue(batchJson, new TypeReference<>() {});

            // Massage all logs before saving
            for (LogEvent log : logs) {
                log.setProcessedTimestamp(System.currentTimeMillis());
                if (log.getMessage() != null)
                    log.setMessage(log.getMessage().trim().toUpperCase());
            }

            // Save all logs at once to the distributed DB
            logRepository.saveAll(logs);

            System.out.println("✅ Saved batch of " + logs.size() + " logs");
        } catch (Exception e) {
            System.err.println("❌ Failed to process batch: " + e.getMessage());
        }
    }
}