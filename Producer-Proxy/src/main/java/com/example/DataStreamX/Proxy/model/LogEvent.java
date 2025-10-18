package com.example.DataStreamX.Proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    private String serviceName;
    private String level;
    private String message;
    private long timestamp;
}
