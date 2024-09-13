package com.ivanalimin.kafkapartitiondemo.consumer;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import com.ivanalimin.kafkapartitiondemo.repository.WebLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class WebLogConsumerTest {

    @MockBean
    private WebLogRepository webLogRepository;

    @Autowired
    private WebLogConsumer webLogConsumer;

    @Test
    void testConsume_nonDuplicateLog_savesLog() {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());
        when(webLogRepository.existsByUserIdAndTimestamp(webLog.getUserId(), webLog.getTimestamp())).thenReturn(false);

        // when
        webLogConsumer.consume(webLog);

        // then
        verify(webLogRepository, times(1)).save(webLog);
    }

    @Test
    void testConsume_duplicateLog_skipsSave() {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());
        when(webLogRepository.existsByUserIdAndTimestamp(webLog.getUserId(), webLog.getTimestamp())).thenReturn(true);

        // when
        webLogConsumer.consume(webLog);

        // then
        verify(webLogRepository, never()).save(webLog);
    }

    @Test
    void testConsume_exceptionThrown_logsError() {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());
        when(webLogRepository.existsByUserIdAndTimestamp(webLog.getUserId(), webLog.getTimestamp())).thenThrow(RuntimeException.class);

        // then
        assertThrows(RuntimeException.class, () -> webLogConsumer.consume(webLog));
    }
}