package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExpressPackage {

    private Long id;
    private String trackingNo;
    private String recipientName;
    private String phone;
    private String pickupCode;
    private String cabinetNo;
    private String status;
    private LocalDateTime arrivedAt;
    private LocalDateTime pickedAt;
}
