package com.training.management.controller;

import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Room;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class PropertyAssetController {

    private final PropertyAssetService propertyAssetService;

    @GetMapping("/properties")
    public ApiResponse<Map<String, Object>> getProperties() {
        return ApiResponse.success(propertyAssetService.getProperties());
    }

    @PostMapping("/buildings")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Building> createBuilding(@RequestBody Building building) {
        return ApiResponse.success("楼栋已创建", propertyAssetService.createBuilding(building));
    }

    @PutMapping("/buildings/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        return ApiResponse.success("楼栋信息已更新", propertyAssetService.updateBuilding(id, building));
    }

    @DeleteMapping("/buildings/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Void> deleteBuilding(@PathVariable Long id) {
        propertyAssetService.deleteBuilding(id);
        return ApiResponse.success("楼栋已删除", null);
    }

    @PostMapping("/rooms")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Room> createRoom(@RequestBody Room room) {
        return ApiResponse.success("房屋已创建", propertyAssetService.createRoom(room));
    }

    @PutMapping("/rooms/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return ApiResponse.success("房屋信息已更新", propertyAssetService.updateRoom(id, room));
    }

    @PatchMapping("/rooms/{id}/resident")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    public ApiResponse<Void> bindRoomResident(
        @PathVariable Long id,
        @RequestParam(required = false) String residentName,
        @RequestParam(required = false) String status
    ) {
        propertyAssetService.bindRoomResident(id, residentName, status);
        return ApiResponse.success("房屋绑定状态已更新", null);
    }

    @DeleteMapping("/rooms/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Void> deleteRoom(@PathVariable Long id) {
        propertyAssetService.deleteRoom(id);
        return ApiResponse.success("房屋已删除", null);
    }
}
