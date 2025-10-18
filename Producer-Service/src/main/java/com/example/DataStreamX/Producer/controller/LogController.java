package com.example.DataStreamX.Producer.controller;

import com.example.DataStreamX.Producer.model.LogEvent;
import com.example.DataStreamX.Producer.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Allow POST /api/logs?level=INFO&message=hi OR JSON body {"level":"INFO","message":"hi"}
    @PostMapping
    public ResponseEntity<String> produceLog(@RequestParam(value = "level", required = false) String level,
                                             @RequestParam(value = "message", required = false) String message,
                                             @RequestBody(required = false) LogEvent body) {
        if (body != null) {
            level = body.getLevel();
            message = body.getMessage();
        }

        if (message == null || message.isBlank()) {
            message = "Manual log at " + System.currentTimeMillis();
        }
        if (level == null || level.isBlank()) {
            level = "INFO";
        }

        logService.sendLog(level, message);
        return ResponseEntity.accepted().body("✅ Log produced and sent to Proxy-Service");
    }
}