package com.training.management.controller;

import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.CommunityActivity;
import com.training.management.domain.entity.ExpressPackage;
import com.training.management.service.SmartService;
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
public class SmartServiceController {

    private final SmartService smartService;

    @GetMapping("/smart-services")
    public ApiResponse<Map<String, Object>> getSmartServices() {
        return ApiResponse.success(smartService.getSmartServices());
    }

    @PostMapping("/activities")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:activity:manage")
    public ApiResponse<CommunityActivity> createActivity(@RequestBody CommunityActivity activity) {
        return ApiResponse.success("社区活动已创建", smartService.createActivity(activity));
    }

    @PutMapping("/activities/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:activity:manage")
    public ApiResponse<CommunityActivity> updateActivity(@PathVariable Long id, @RequestBody CommunityActivity activity) {
        return ApiResponse.success("社区活动已更新", smartService.updateActivity(id, activity));
    }

    @PatchMapping("/activities/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:activity:manage")
    public ApiResponse<Void> updateActivityStatus(@PathVariable Long id, @RequestParam String status) {
        smartService.updateActivityStatus(id, status);
        return ApiResponse.success("社区活动状态已更新", null);
    }

    @DeleteMapping("/activities/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:activity:manage")
    public ApiResponse<Void> deleteActivity(@PathVariable Long id) {
        smartService.deleteActivity(id);
        return ApiResponse.success("社区活动已删除", null);
    }

    @PostMapping("/packages")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:package:manage")
    public ApiResponse<ExpressPackage> createPackage(@RequestBody ExpressPackage expressPackage) {
        return ApiResponse.success("快递代收已登记", smartService.createPackage(expressPackage));
    }

    @PutMapping("/packages/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:package:manage")
    public ApiResponse<ExpressPackage> updatePackage(@PathVariable Long id, @RequestBody ExpressPackage expressPackage) {
        return ApiResponse.success("快递信息已更新", smartService.updatePackage(id, expressPackage));
    }

    @PatchMapping("/packages/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:package:manage")
    public ApiResponse<Void> updatePackageStatus(@PathVariable Long id, @RequestParam String status) {
        smartService.updatePackageStatus(id, status);
        return ApiResponse.success("快递状态已更新", null);
    }

    @DeleteMapping("/packages/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:package:manage")
    public ApiResponse<Void> deletePackage(@PathVariable Long id) {
        smartService.deletePackage(id);
        return ApiResponse.success("快递记录已删除", null);
    }
}
