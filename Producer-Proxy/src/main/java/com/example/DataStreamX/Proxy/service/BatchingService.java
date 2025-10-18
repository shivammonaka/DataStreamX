package com.example.DataStreamX.Proxy.service;

import com.example.DataStreamX.Proxy.model.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BatchingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.producer.publish-topic}")
    private String kafkaTopic;

    // Configurable batching thresholds
    private static final int MAX_COUNT = 500;     // batch by count
    private static final int MAX_SIZE_BYTES = 64 * 1024; // batch by size
    private final List<LogEvent> buffer = new ArrayList<>();
    private int currentBatchSize = 0;

    public BatchingService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public synchronized void addMessage(LogEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            buffer.add(event);
            currentBatchSize += json.getBytes().length;

            if (buffer.size() >= MAX_COUNT || currentBatchSize >= MAX_SIZE_BYTES) {
                flush();
            }
        } catch (Exception e) {
            log.error("Error buffering message", e);
        }
    }

    @Scheduled(fixedRate = 50) // flush every 50ms as backup
    public synchronized void scheduledFlush() {
        if (!buffer.isEmpty()) flush();
    }

    private synchronized void flush() {
        if (buffer.isEmpty()) return;
        try {
            log.info("Flushing {} messages to Kafka", buffer.size());
            // Serialize the entire batch into a JSON array
            String batchJson = objectMapper.writeValueAsString(buffer);
            // Send the batch as a single Kafka message
            kafkaTemplate.send(kafkaTopic, batchJson);
        } catch (Exception e) {
            log.error("Failed to flush batch", e);
        } finally {
            buffer.clear();
            currentBatchSize = 0;
        }
    }
}
