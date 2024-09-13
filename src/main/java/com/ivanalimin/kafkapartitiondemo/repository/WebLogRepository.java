package com.ivanalimin.kafkapartitiondemo.repository;

import com.ivanalimin.kafkapartitiondemo.model.WebLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface WebLogRepository extends JpaRepository<WebLog, UUID> {
    boolean existsByUserIdAndTimestamp(String userId, LocalDateTime timestamp);
}
