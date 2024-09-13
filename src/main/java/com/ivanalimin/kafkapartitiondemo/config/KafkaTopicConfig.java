package com.ivanalimin.kafkapartitiondemo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topics.web-logs.partitions}")
    private int numberOfPartitions;

    @Bean
    public NewTopic webLogsTopic() {
        return new NewTopic("web-logs", numberOfPartitions, (short) 1);
    }
}
