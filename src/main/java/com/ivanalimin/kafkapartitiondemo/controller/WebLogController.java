package com.ivanalimin.kafkapartitiondemo.controller;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import com.ivanalimin.kafkapartitiondemo.producer.WebLogProducer;
import com.ivanalimin.kafkapartitiondemo.repository.WebLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/logs")
@RequiredArgsConstructor
public class WebLogController {

    private final WebLogProducer webLogProducer;
    private final WebLogRepository webLogRepository;

    @PostMapping
    public ResponseEntity<String> sendLog(@RequestBody WebLog webLog) {
        webLogProducer.sendLog(webLog);
        return ResponseEntity.ok("Log sent to Kafka topic 'web-logs'.");
    }

    @GetMapping
    public List<WebLog> findAll() {
        return webLogRepository.findAll();
    }
}
