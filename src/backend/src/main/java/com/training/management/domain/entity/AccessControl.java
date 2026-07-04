package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccessControl {

    private Long id;
    private String deviceName;
    private String gateName;
    private String deviceType;
    private String status;
    private String manager;
    private LocalDateTime lastCheckAt;
}
