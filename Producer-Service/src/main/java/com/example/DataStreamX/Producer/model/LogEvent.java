package com.example.DataStreamX.Producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple DTO for a log event that will be serialized to JSON and sent to Kafka.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    private String serviceName;
    private String level;
    private String message;
    private long timestamp;
}