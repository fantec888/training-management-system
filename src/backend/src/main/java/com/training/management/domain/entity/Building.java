package com.training.management.domain.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Building {

    private Long id;
    private String name;
    private Integer floors;
    private Integer units;
    private BigDecimal occupancyRate;
    private Integer vacantCount;
    private String manager;
    private String status;
}
