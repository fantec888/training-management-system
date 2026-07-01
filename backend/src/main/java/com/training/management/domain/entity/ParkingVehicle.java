package com.training.management.domain.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ParkingVehicle {

    private Long id;
    private String plate;
    private String owner;
    private String vehicleType;
    private String location;
    private String status;
    private BigDecimal monthlyFee;
}
