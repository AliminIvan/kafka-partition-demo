package com.ivanalimin.kafkapartitiondemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import com.ivanalimin.kafkapartitiondemo.producer.WebLogProducer;
import com.ivanalimin.kafkapartitiondemo.repository.WebLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class WebLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebLogProducer webLogProducer;

    @MockBean
    private WebLogRepository webLogRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSendLog_returnsOk() throws Exception {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());

        // when & then
        mockMvc.perform(post("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webLog)))
                .andExpect(status().isOk())
                .andExpect(content().string("Log sent to Kafka topic 'web-logs'."));

        verify(webLogProducer, times(1)).sendLog(any(WebLog.class));
    }

    @Test
    void testFindAll_returnsLogs() throws Exception {
        // given
        WebLog webLog = new WebLog(UUID.randomUUID(), "user1", "action1", LocalDateTime.now());
        when(webLogRepository.findAll()).thenReturn(Collections.singletonList(webLog));

        // when & then
        mockMvc.perform(get("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].action").value("action1"));

        verify(webLogRepository, times(1)).findAll();
    }
}