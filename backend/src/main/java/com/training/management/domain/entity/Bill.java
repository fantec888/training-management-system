package com.training.management.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Bill {

    private Long id;
    private String residentName;
    private String house;
    private String feeType;
    private String billPeriod;
    private BigDecimal amount;
    private String status;
    private LocalDate dueDate;
}
