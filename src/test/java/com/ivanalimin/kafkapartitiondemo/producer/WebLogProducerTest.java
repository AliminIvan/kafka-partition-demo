package com.ivanalimin.kafkapartitiondemo.producer;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class WebLogProducerTest {

    @MockBean
    private KafkaTemplate<String, WebLog> kafkaTemplate;

    @Autowired
    private WebLogProducer webLogProducer;

    @Test
    void testSendLog_successfulSend() {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());

        // when
        webLogProducer.sendLog(webLog);

        // then
        verify(kafkaTemplate, times(1)).send("web-logs", webLog.getUserId(), webLog);
    }

    @Test
    void testSendLog_generatesUUIDAndTimestamp() {
        // given
        WebLog webLog = new WebLog(null, "user1", "action1", null);

        // when
        webLogProducer.sendLog(webLog);

        // then
        assertNotNull(webLog.getId());
        assertNotNull(webLog.getTimestamp());
        verify(kafkaTemplate, times(1)).send("web-logs", webLog.getUserId(), webLog);
    }
}