package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Community {

    private Long id;
    private String name;
    private String address;
    private String developer;
    private String propertyCompany;
    private Integer totalBuildings;
    private Integer totalRooms;
    private String manager;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
