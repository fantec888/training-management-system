package com.training.management.domain.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SysPermission {

    private Long id;
    private String permissionCode;
    private String permissionName;
    private String permissionType;
    private Long parentId;
    private String routePath;
    private String componentKey;
    private Integer sortOrder;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
