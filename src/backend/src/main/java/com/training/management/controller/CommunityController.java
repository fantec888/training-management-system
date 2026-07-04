package com.training.management.controller;

import java.util.List;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Community;
import com.training.management.service.PropertyAssetService;
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
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final PropertyAssetService propertyAssetService;

    @GetMapping
    public ApiResponse<List<Community>> listCommunities() {
        return ApiResponse.success(propertyAssetService.listCommunities());
    }

    @PostMapping
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:community:manage")
    public ApiResponse<Community> createCommunity(@RequestBody Community community) {
        return ApiResponse.success("小区创建成功", propertyAssetService.createCommunity(community));
    }

    @PutMapping("/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:community:manage")
    public ApiResponse<Community> updateCommunity(@PathVariable Long id, @RequestBody Community community) {
        return ApiResponse.success("小区信息已更新", propertyAssetService.updateCommunity(id, community));
    }

    @DeleteMapping("/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:community:manage")
    public ApiResponse<Void> deleteCommunity(@PathVariable Long id) {
        propertyAssetService.deleteCommunity(id);
        return ApiResponse.success("小区已删除", null);
    }
}
