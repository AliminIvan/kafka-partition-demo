package com.ivanalimin.kafkapartitiondemo.producer;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebLogProducer {

    private final KafkaTemplate<String, WebLog> kafkaTemplate;

    public void sendLog(WebLog webLog) {
        if (webLog.getId() == null) {
            webLog.setId(UUID.randomUUID());
        }
        if (webLog.getTimestamp() == null) {
            webLog.setTimestamp(LocalDateTime.now());
        }
        kafkaTemplate.send("web-logs", webLog.getUserId(), webLog);
        log.info("Send log: {}", webLog);
    }
}
