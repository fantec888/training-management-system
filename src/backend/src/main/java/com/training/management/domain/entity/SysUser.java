package com.training.management.domain.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SysUser {

    private Long id;
    private String username;
    @JsonIgnore
    private String passwordHash;
    private String realName;
    private String roleCode;
    private String department;
    private String permissionScope;
    private String phone;
    private Boolean enabled;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
