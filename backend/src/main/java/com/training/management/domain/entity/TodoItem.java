package com.training.management.domain.entity;

import lombok.Data;

@Data
public class TodoItem {

    private Long id;
    private String title;
    private String deadline;
    private String level;
    private Boolean completed;
}
