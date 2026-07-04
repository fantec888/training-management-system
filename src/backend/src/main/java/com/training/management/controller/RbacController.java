package com.training.management.controller;

import java.util.List;
import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RequestContext;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.dto.RolePermissionRequest;
import com.training.management.domain.dto.UserRoleRequest;
import com.training.management.domain.entity.SysPermission;
import com.training.management.domain.entity.SysRole;
import com.training.management.service.RbacService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rbac")
@RequiredArgsConstructor
public class RbacController {

    private final RbacService rbacService;

    @GetMapping("/current")
    public ApiResponse<Map<String, Object>> current() {
        return ApiResponse.success(rbacService.buildCurrentPermission(RequestContext.getCurrentUser()));
    }

    @GetMapping("/roles")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    public ApiResponse<List<SysRole>> listRoles() {
        return ApiResponse.success(rbacService.listRoles());
    }

    @PostMapping("/roles")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:role:manage")
    public ApiResponse<SysRole> createRole(@RequestBody SysRole role) {
        return ApiResponse.success("角色创建成功", rbacService.createRole(role));
    }

    @PutMapping("/roles/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:role:manage")
    public ApiResponse<SysRole> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        return ApiResponse.success("角色更新成功", rbacService.updateRole(id, role));
    }

    @DeleteMapping("/roles/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:role:manage")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        rbacService.deleteRole(id);
        return ApiResponse.success("角色删除成功", null);
    }

    @GetMapping("/permissions")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    public ApiResponse<List<SysPermission>> listPermissions() {
        return ApiResponse.success(rbacService.listPermissions());
    }

    @PostMapping("/permissions")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:permission:manage")
    public ApiResponse<SysPermission> createPermission(@RequestBody SysPermission permission) {
        return ApiResponse.success("权限创建成功", rbacService.createPermission(permission));
    }

    @PutMapping("/permissions/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:permission:manage")
    public ApiResponse<SysPermission> updatePermission(@PathVariable Long id, @RequestBody SysPermission permission) {
        return ApiResponse.success("权限更新成功", rbacService.updatePermission(id, permission));
    }

    @DeleteMapping("/permissions/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:permission:manage")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        rbacService.deletePermission(id);
        return ApiResponse.success("权限删除成功", null);
    }

    @GetMapping("/users/{userId}/roles")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    public ApiResponse<List<String>> listUserRoles(@PathVariable Long userId) {
        return ApiResponse.success(rbacService.listUserRoleCodes(userId));
    }

    @PutMapping("/users/{userId}/roles")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:user-role:assign")
    public ApiResponse<Void> assignUserRoles(@PathVariable Long userId, @RequestBody UserRoleRequest request) {
        rbacService.assignUserRoles(userId, request.getRoleCodes());
        return ApiResponse.success("用户角色分配成功", null);
    }

    @GetMapping("/roles/{roleId}/permissions")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    public ApiResponse<List<String>> listRolePermissions(@PathVariable Long roleId) {
        return ApiResponse.success(rbacService.listRolePermissionCodes(roleId));
    }

    @PutMapping("/roles/{roleId}/permissions")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:role-permission:assign")
    public ApiResponse<Void> assignRolePermissions(@PathVariable Long roleId, @RequestBody RolePermissionRequest request) {
        rbacService.assignRolePermissions(roleId, request.getPermissionCodes());
        return ApiResponse.success("角色权限分配成功", null);
    }
}
