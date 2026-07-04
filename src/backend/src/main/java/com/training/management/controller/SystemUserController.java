package com.training.management.controller;

import java.util.List;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.SysUser;
import com.training.management.service.SystemAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system-users")
@RequiredArgsConstructor
@RequireRole(RoleCodes.SUPER_ADMIN)
public class SystemUserController {

    private final SystemAccountService systemAccountService;

    @GetMapping
    public ApiResponse<List<SysUser>> listSystemUsers() {
        return ApiResponse.success(systemAccountService.listSystemUsers());
    }

    @PostMapping
    @RequirePermission("button:user:create")
    public ApiResponse<SysUser> createSystemUser(@RequestBody SysUser user) {
        return ApiResponse.success("账号创建成功", systemAccountService.createSystemUser(user));
    }

    @PutMapping("/{id}")
    @RequirePermission("button:user:update")
    public ApiResponse<SysUser> updateSystemUser(@PathVariable Long id, @RequestBody SysUser user) {
        return ApiResponse.success("账号信息已更新", systemAccountService.updateSystemUser(id, user));
    }

    @PatchMapping("/{id}/status")
    @RequirePermission("button:user:update")
    public ApiResponse<Void> updateSystemUserStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        systemAccountService.updateSystemUserStatus(id, enabled);
        return ApiResponse.success("账号状态已更新", null);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("button:user:delete")
    public ApiResponse<Void> deleteSystemUser(@PathVariable Long id) {
        systemAccountService.deleteSystemUser(id);
        return ApiResponse.success("账号已删除", null);
    }
}
