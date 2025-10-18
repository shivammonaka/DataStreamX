package com.example.DataStreamX.Proxy.controller;

import com.example.DataStreamX.Proxy.service.BatchingService;
import com.example.DataStreamX.Proxy.model.LogEvent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/logs")
public class ProxyController {

    private final BatchingService batchingService;

    public ProxyController(BatchingService batchingService) {
        this.batchingService = batchingService;
    }

    @PostMapping
    public String publishLog(@RequestBody LogEvent logEvent) {
        batchingService.addMessage(logEvent);
        return "Accepted";
    }
}
