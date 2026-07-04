package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Complaint {

    private Long id;
    private String code;
    private String residentName;
    private String phone;
    private String category;
    private String title;
    private String content;
    private String handler;
    private String reply;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime repliedAt;
}
