package com.training.management.controller;

import java.util.List;
import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.PageResult;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.Room;
import com.training.management.domain.entity.SysUser;
import com.training.management.service.PropertyService;
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
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/residents")
    public ApiResponse<List<Resident>> listResidents() {
        return ApiResponse.success(propertyService.listResidents());
    }

    @GetMapping("/residents/page")
    public ApiResponse<PageResult<Resident>> pageResidents(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "5") Integer pageSize
    ) {
        return ApiResponse.success(propertyService.pageResidents(pageNum, pageSize));
    }

    @PostMapping("/residents")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:create")
    public ApiResponse<Resident> createResident(@RequestBody Resident resident) {
        return ApiResponse.success("新增住户成功", propertyService.createResident(resident));
    }

    @PutMapping("/residents/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:update")
    public ApiResponse<Resident> updateResident(@PathVariable Long id, @RequestBody Resident resident) {
        return ApiResponse.success("住户信息已更新", propertyService.updateResident(id, resident));
    }

    @PatchMapping("/residents/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:update")
    public ApiResponse<Void> updateResidentStatus(@PathVariable Long id, @RequestParam String status) {
        propertyService.updateResidentStatus(id, status);
        return ApiResponse.success("住户状态已更新", null);
    }

    @DeleteMapping("/residents/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:resident:delete")
    public ApiResponse<Void> deleteResident(@PathVariable Long id) {
        propertyService.deleteResident(id);
        return ApiResponse.success("住户已删除", null);
    }

    @GetMapping("/properties")
    public ApiResponse<Map<String, Object>> getProperties() {
        return ApiResponse.success(propertyService.getProperties());
    }

    @PostMapping("/buildings")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Building> createBuilding(@RequestBody Building building) {
        return ApiResponse.success("楼栋已创建", propertyService.createBuilding(building));
    }

    @PutMapping("/buildings/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        return ApiResponse.success("楼栋信息已更新", propertyService.updateBuilding(id, building));
    }

    @DeleteMapping("/buildings/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Void> deleteBuilding(@PathVariable Long id) {
        propertyService.deleteBuilding(id);
        return ApiResponse.success("楼栋已删除", null);
    }

    @PostMapping("/rooms")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Room> createRoom(@RequestBody Room room) {
        return ApiResponse.success("房屋已创建", propertyService.createRoom(room));
    }

    @PutMapping("/rooms/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return ApiResponse.success("房屋信息已更新", propertyService.updateRoom(id, room));
    }

    @PatchMapping("/rooms/{id}/resident")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    public ApiResponse<Void> bindRoomResident(
        @PathVariable Long id,
        @RequestParam(required = false) String residentName,
        @RequestParam(required = false) String status
    ) {
        propertyService.bindRoomResident(id, residentName, status);
        return ApiResponse.success("房屋绑定状态已更新", null);
    }

    @DeleteMapping("/rooms/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    public ApiResponse<Void> deleteRoom(@PathVariable Long id) {
        propertyService.deleteRoom(id);
        return ApiResponse.success("房屋已删除", null);
    }

    @GetMapping("/repairs")
    public ApiResponse<List<RepairOrder>> listRepairs() {
        return ApiResponse.success(propertyService.listRepairs());
    }

    @PostMapping("/repairs")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:repair:create")
    public ApiResponse<RepairOrder> createRepair(@RequestBody RepairOrder repairOrder) {
        return ApiResponse.success("工单创建成功", propertyService.createRepair(repairOrder));
    }

    @PatchMapping("/repairs/{id}/progress")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:repair:update")
    public ApiResponse<Void> updateRepairProgress(@PathVariable Long id, @RequestBody RepairOrder repairOrder) {
        propertyService.updateRepairProgress(id, repairOrder);
        return ApiResponse.success("工单进度已更新", null);
    }

    @DeleteMapping("/repairs/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:repair:delete")
    public ApiResponse<Void> deleteRepair(@PathVariable Long id) {
        propertyService.deleteRepair(id);
        return ApiResponse.success("工单已删除", null);
    }

    @GetMapping("/billing")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    public ApiResponse<Map<String, Object>> getBilling() {
        return ApiResponse.success(propertyService.getBilling());
    }

    @PostMapping("/billing")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:create")
    public ApiResponse<Bill> createBill(@RequestBody Bill bill) {
        return ApiResponse.success("账单创建成功", propertyService.createBill(bill));
    }

    @PatchMapping("/billing/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:update")
    public ApiResponse<Void> updateBillStatus(@PathVariable Long id, @RequestParam String status) {
        propertyService.updateBillStatus(id, status);
        return ApiResponse.success("账单状态已更新", null);
    }

    @DeleteMapping("/billing/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:delete")
    public ApiResponse<Void> deleteBill(@PathVariable Long id) {
        propertyService.deleteBill(id);
        return ApiResponse.success("账单已删除", null);
    }

    @GetMapping("/parking")
    public ApiResponse<Map<String, Object>> getParking() {
        return ApiResponse.success(propertyService.getParking());
    }

    @GetMapping("/notices")
    public ApiResponse<Map<String, Object>> getNotices() {
        return ApiResponse.success(propertyService.getNotices());
    }

    @PostMapping("/notices")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:create")
    public ApiResponse<Notice> createNotice(@RequestBody Notice notice) {
        return ApiResponse.success("公告发布成功", propertyService.createNotice(notice));
    }

    @PatchMapping("/notices/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:create")
    public ApiResponse<Void> updateNoticeStatus(@PathVariable Long id, @RequestParam String status) {
        propertyService.updateNoticeStatus(id, status);
        return ApiResponse.success("公告状态已更新", null);
    }

    @DeleteMapping("/notices/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:delete")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        propertyService.deleteNotice(id);
        return ApiResponse.success("公告已删除", null);
    }

    @GetMapping("/system-users")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    public ApiResponse<List<SysUser>> listSystemUsers() {
        return ApiResponse.success(propertyService.listSystemUsers());
    }

    @PostMapping("/system-users")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:user:create")
    public ApiResponse<SysUser> createSystemUser(@RequestBody SysUser user) {
        return ApiResponse.success("账号创建成功", propertyService.createSystemUser(user));
    }

    @PutMapping("/system-users/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:user:update")
    public ApiResponse<SysUser> updateSystemUser(@PathVariable Long id, @RequestBody SysUser user) {
        return ApiResponse.success("账号信息已更新", propertyService.updateSystemUser(id, user));
    }

    @PatchMapping("/system-users/{id}/status")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:user:update")
    public ApiResponse<Void> updateSystemUserStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        propertyService.updateSystemUserStatus(id, enabled);
        return ApiResponse.success("账号状态已更新", null);
    }

    @DeleteMapping("/system-users/{id}")
    @RequireRole(RoleCodes.SUPER_ADMIN)
    @RequirePermission("button:user:delete")
    public ApiResponse<Void> deleteSystemUser(@PathVariable Long id) {
        propertyService.deleteSystemUser(id);
        return ApiResponse.success("账号已删除", null);
    }
}
