package com.training.management.controller;

import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.AccessControl;
import com.training.management.domain.entity.PatrolTask;
import com.training.management.service.SecurityService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping("/parking")
    public ApiResponse<Map<String, Object>> getParking() {
        return ApiResponse.success(securityService.getParking());
    }

    @GetMapping("/security")
    public ApiResponse<Map<String, Object>> getSecurity() {
        return ApiResponse.success(securityService.getSecurity());
    }

    @PostMapping("/access-controls")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:access:manage")
    public ApiResponse<AccessControl> createAccessControl(@RequestBody AccessControl accessControl) {
        return ApiResponse.success("门禁设备已创建", securityService.createAccessControl(accessControl));
    }

    @PutMapping("/access-controls/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:access:manage")
    public ApiResponse<AccessControl> updateAccessControl(@PathVariable Long id, @RequestBody AccessControl accessControl) {
        return ApiResponse.success("门禁设备已更新", securityService.updateAccessControl(id, accessControl));
    }

    @PatchMapping("/access-controls/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:access:manage")
    public ApiResponse<Void> updateAccessStatus(@PathVariable Long id, @RequestParam String status) {
        securityService.updateAccessStatus(id, status);
        return ApiResponse.success("门禁状态已更新", null);
    }

    @DeleteMapping("/access-controls/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:access:manage")
    public ApiResponse<Void> deleteAccessControl(@PathVariable Long id) {
        securityService.deleteAccessControl(id);
        return ApiResponse.success("门禁设备已删除", null);
    }

    @PostMapping("/patrols")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:patrol:manage")
    public ApiResponse<PatrolTask> createPatrol(@RequestBody PatrolTask patrolTask) {
        return ApiResponse.success("巡检任务已创建", securityService.createPatrol(patrolTask));
    }

    @PutMapping("/patrols/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:patrol:manage")
    public ApiResponse<PatrolTask> updatePatrol(@PathVariable Long id, @RequestBody PatrolTask patrolTask) {
        return ApiResponse.success("巡检任务已更新", securityService.updatePatrol(id, patrolTask));
    }

    @PatchMapping("/patrols/{id}/finish")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:patrol:manage")
    public ApiResponse<Void> finishPatrol(@PathVariable Long id, @RequestBody PatrolTask patrolTask) {
        securityService.finishPatrol(id, patrolTask);
        return ApiResponse.success("巡检结果已保存", null);
    }

    @DeleteMapping("/patrols/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:patrol:manage")
    public ApiResponse<Void> deletePatrol(@PathVariable Long id) {
        securityService.deletePatrol(id);
        return ApiResponse.success("巡检任务已删除", null);
    }
}
