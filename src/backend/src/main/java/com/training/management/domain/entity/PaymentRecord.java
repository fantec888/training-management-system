package com.training.management.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentRecord {

    private Long id;
    private Long billId;
    private String payerName;
    private String house;
    private BigDecimal amount;
    private String paymentMethod;
    private String operator;
    private String remark;
    private LocalDateTime paidAt;
}
