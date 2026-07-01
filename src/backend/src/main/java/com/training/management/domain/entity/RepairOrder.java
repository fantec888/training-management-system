package com.training.management.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RepairOrder {

    private Long id;
    private String code;
    private String title;
    private String area;
    private String priority;
    private String assignee;
    private String status;
    private BigDecimal durationHours;
    private LocalDateTime createdAt;
}
