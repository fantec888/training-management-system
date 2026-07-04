package com.training.management.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRoleRequest {

    private List<String> roleCodes;
}
