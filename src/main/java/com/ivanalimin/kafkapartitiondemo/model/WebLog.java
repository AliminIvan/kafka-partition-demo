package com.ivanalimin.kafkapartitiondemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "web_logs")
public class WebLog {

    @Id
    @Column(nullable = false, unique = true)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String userId;

    @NotBlank
    @Column(nullable = false)
    private String action;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "WebLog{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
