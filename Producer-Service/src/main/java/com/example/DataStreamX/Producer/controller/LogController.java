package com.example.DataStreamX.Producer.controller;

import com.example.DataStreamX.Producer.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Minimal REST controller to allow manual creation of logs.
 * POST /api/logs { "level": "...", "message": "..." }
 * GET /api/logs/ping -> quick health/verify endpoint
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Producer-Service is up");
    }

    @PostMapping
    public ResponseEntity<String> produceLog(@RequestParam(value = "level", required = false) String level,
                                             @RequestParam(value = "message", required = false) String message) {
        if (message == null || message.isBlank()) {
            message = "Manual log at " + System.currentTimeMillis();
        }
        if (level == null || level.isBlank()) {
            level = "INFO";
        }
        logService.sendLog(level, message);
        return ResponseEntity.accepted().body("Log produced");
    }
}