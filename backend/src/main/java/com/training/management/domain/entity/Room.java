package com.training.management.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Room {

    private Long id;
    private String buildingName;
    private String unitNo;
    private String roomNo;
    private Integer floorNo;
    private BigDecimal area;
    private String residentName;
    private String status;
    private LocalDateTime updatedAt;
}
