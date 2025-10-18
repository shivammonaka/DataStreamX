package com.example.DataStreamX;

import com.example.DataStreamX.Producer.service.ProxyHealthService;
import com.example.DataStreamX.Producer.service.LogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerServiceApplication.class, args);
    }

    /**
     * Wait for Proxy-Service before starting log generation.
     */
    @Bean
    public CommandLineRunner startupChecker(ProxyHealthService proxyHealthService, LogService logService) {
        return args -> {
            proxyHealthService.waitForProxy(50, 3000); // 50 retries × 3s = 150s max
            logService.activateLogging();
        };
    }
}