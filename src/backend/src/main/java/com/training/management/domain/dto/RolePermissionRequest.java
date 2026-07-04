package com.training.management.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class RolePermissionRequest {

    private List<String> permissionCodes;
}
