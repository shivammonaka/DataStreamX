package com.example.DataStreamX.Producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single log event emitted by any microservice.
 * Will be serialized to JSON and forwarded to the Proxy-Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {

    private String microServiceName; // name of microservice generating the log
    private String level;            // log level: INFO, ERROR, DEBUG, etc.
    private String message;          // log message content
    private long timestamp;          // original event timestamp (epoch millis)
}