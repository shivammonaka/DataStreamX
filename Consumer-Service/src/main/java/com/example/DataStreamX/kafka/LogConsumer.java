package com.example.DataStreamX.kafka;

import com.example.DataStreamX.Consumer.service.LogProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class LogConsumer {

    private final LogProcessingService logProcessingService;

    public LogConsumer(LogProcessingService logProcessingService) {
        this.logProcessingService = logProcessingService;
    }

    @KafkaListener(topics = "logs-topic", groupId = "log-consumer-group")
    public void consume(String message, Acknowledgment ack) {
        try {
            // message is now a JSON array of logs
            logProcessingService.processBatch(message);
            ack.acknowledge(); // commit only after successful DB save
        } catch (Exception e) {
            System.err.println("Failed to process Kafka batch: " + e.getMessage());
        }
    }
}