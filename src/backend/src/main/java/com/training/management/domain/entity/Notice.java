package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Notice {

    private Long id;
    private String title;
    private String category;
    private String audience;
    private String publisher;
    private LocalDateTime publishTime;
    private String status;
}
