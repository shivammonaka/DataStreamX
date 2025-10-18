package com.example.DataStreamX.Producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProxyHealthService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyHealthService.class);

    private final RestTemplate restTemplate;

    @Value("${producer-proxy.url.ping}")
    private String healthUrl; // e.g. http://localhost:8081/api/v1/logs

    public ProxyHealthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Check if Proxy-Service is up using its /ping endpoint.
     */
    public boolean isProxyUp() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            boolean healthy = response.getStatusCode().is2xxSuccessful();
            if (!healthy)
                logger.warn("⚠️ Proxy-Service ping failed: {}", response.getStatusCode());
            return healthy;
        } catch (Exception e) {
            logger.warn("❌ Proxy-Service not reachable: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Try repeatedly until Proxy is available.
     */
    public void waitForProxy(int maxRetries, long delayMs) {
        int attempts = 0;
        while (attempts < maxRetries) {
            if (isProxyUp()) {
                logger.info("✅ Proxy-Service is up after {} attempt(s).", attempts + 1);
                return;
            }
            attempts++;
            try {
                logger.info("⏳ Waiting for Proxy-Service... (attempt {}/{})", attempts, maxRetries);
                Thread.sleep(delayMs);
            } catch (InterruptedException ignored) {}
        }
        logger.error("🚨 Proxy-Service not available after {} retries.", maxRetries);
    }
}