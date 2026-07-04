package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommunityActivity {

    private Long id;
    private String title;
    private String activityType;
    private String location;
    private String organizer;
    private LocalDateTime startTime;
    private Integer signups;
    private String status;
    private LocalDateTime createdAt;
}
