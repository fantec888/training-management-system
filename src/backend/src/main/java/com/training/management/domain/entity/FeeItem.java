package com.training.management.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FeeItem {

    private Long id;
    private String name;
    private String category;
    private String billingCycle;
    private BigDecimal unitPrice;
    private String description;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
