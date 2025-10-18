package com.example.DataStreamX;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProducerProxyApplication.class, args);
    }
}
