package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PatrolTask {

    private Long id;
    private String routeName;
    private String area;
    private String assignee;
    private LocalDateTime planTime;
    private String status;
    private String result;
    private LocalDateTime finishedAt;
}
