package com.training.management.domain.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MonthlyRevenue {

    private Long id;
    private String monthLabel;
    private BigDecimal amount;
}
