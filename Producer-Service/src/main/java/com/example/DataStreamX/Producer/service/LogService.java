package com.example.DataStreamX.Producer.service;

import com.example.DataStreamX.Producer.model.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Random;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @Value("${producer-proxy.url}")
    private String producerProxyUrl; // e.g., http://localhost:8081/api/v1/logs

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${app.logging.levels}")
    private String logLevelsCsv;

    private String[] logLevels;

    public LogService(@Value("${app.logging.levels}") String logLevelsCsv) {
        this.logLevels = parseLevels(logLevelsCsv);
    }

    private String[] parseLevels(String csv) {
        if (csv == null || csv.isBlank()) {
            return new String[]{"INFO", "ERROR"};
        }
        return csv.split("\\s*,\\s*");
    }

    public void sendRandomLog() {
        String level = logLevels[random.nextInt(logLevels.length)];
        String message = "Auto-generated log message #" + random.nextInt(1000);
        sendLog(level, message);
    }

    public void sendLog(String level, String message) {
        LogEvent event = new LogEvent(serviceName, level, message, Instant.now().toEpochMilli());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String json = objectMapper.writeValueAsString(event);
            HttpEntity<String> request = new HttpEntity<>(json, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(producerProxyUrl, request, String.class);

            logger.info("Sent log to Proxy-Service [{}]: status={}, body={}",
                    producerProxyUrl, response.getStatusCode(), response.getBody());

        } catch (Exception e) {
            logger.error("Failed to send log to Proxy-Service: {}", e.getMessage(), e);
        }
    }
}
