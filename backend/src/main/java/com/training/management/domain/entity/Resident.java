package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Resident {

    private Long id;
    private String name;
    private String identityType;
    private String phone;
    private String building;
    private String roomNo;
    private Integer vehicles;
    private String verifiedStatus;
    private String tag;
    private String status;
    private LocalDateTime createdAt;
}
