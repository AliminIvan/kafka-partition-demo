package com.ivanalimin.kafkapartitiondemo.consumer;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import com.ivanalimin.kafkapartitiondemo.repository.WebLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebLogConsumer {

    private final WebLogRepository webLogRepository;

    @KafkaListener(topics = "web-logs", groupId = "web-log-consumers", concurrency = "3")
    public void consume(WebLog webLog) {
        try {
            if (!isDuplicateLog(webLog)) {
                webLogRepository.save(webLog);
                log.info("Saved log for user: {}, action: {}", webLog.getUserId(), webLog.getAction());
            } else {
                log.info("Duplicate log detected for user: {}, action: {}, skipping save",
                        webLog.getUserId(), webLog.getAction());
            }
        } catch (Exception exception) {
            log.error("Error processing log for user: {}, action: {}", webLog.getUserId(), webLog.getAction(), exception);
            throw exception;
        }
    }

    private boolean isDuplicateLog(WebLog webLog) {
        return webLogRepository.existsByUserIdAndTimestamp(webLog.getUserId(), webLog.getTimestamp());
    }
}
