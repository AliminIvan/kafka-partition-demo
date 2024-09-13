package com.ivanalimin.kafkapartitiondemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler kafkaDefaultErrorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);
        return new DefaultErrorHandler((consumerRecord, exception) -> {
            if (exception instanceof RetriableException) {
                log.warn("Retriable exception occurred for record with key: {}, value: {}, retrying...",
                        consumerRecord.key(), consumerRecord.value(), exception);
            } else {
                log.error("All retries failed for record with key: {}, value: {}",
                        consumerRecord.key(), consumerRecord.value(), exception);
            }
        },
                fixedBackOff);
    }
}
