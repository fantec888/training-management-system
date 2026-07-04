package com.training.management.controller;

import java.util.List;

import com.training.management.common.ApiResponse;
import com.training.management.common.PageResult;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Resident;
import com.training.management.service.PropertyAssetService;
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
@RequestMapping("/api/residents")
@RequiredArgsConstructor
public class ResidentController {

    private final PropertyAssetService propertyAssetService;

    @GetMapping
    public ApiResponse<List<Resident>> listResidents() {
        return ApiResponse.success(propertyAssetService.listResidents());
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<Resident>> pageResidents(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return ApiResponse.success(propertyAssetService.pageResidents(pageNum, pageSize));
    }

    @PostMapping
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:create")
    public ApiResponse<Resident> createResident(@RequestBody Resident resident) {
        return ApiResponse.success("新增住户成功", propertyAssetService.createResident(resident));
    }

    @PutMapping("/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:update")
    public ApiResponse<Resident> updateResident(@PathVariable Long id, @RequestBody Resident resident) {
        return ApiResponse.success("住户信息已更新", propertyAssetService.updateResident(id, resident));
    }

    @PatchMapping("/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:update")
    public ApiResponse<Void> updateResidentStatus(@PathVariable Long id, @RequestParam String status) {
        propertyAssetService.updateResidentStatus(id, status);
        return ApiResponse.success("住户状态已更新", null);
    }

    @DeleteMapping("/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:delete")
    public ApiResponse<Void> deleteResident(@PathVariable Long id) {
        propertyAssetService.deleteResident(id);
        return ApiResponse.success("住户已删除", null);
    }
}
