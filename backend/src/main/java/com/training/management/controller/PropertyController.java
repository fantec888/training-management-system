package com.training.management.controller;

import java.util.List;
import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.Room;
import com.training.management.domain.entity.SysUser;
import com.training.management.service.PropertyService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/residents")
    public ApiResponse<Resident> createResident(@RequestBody Resident resident) {
        return ApiResponse.success("新增住户成功", propertyService.createResident(resident));
    }

    @PutMapping("/residents/{id}")
    public ApiResponse<Resident> updateResident(@PathVariable Long id, @RequestBody Resident resident) {
        return ApiResponse.success("住户信息已更新", propertyService.updateResident(id, resident));
    }

    @PatchMapping("/residents/{id}/status")
    public ApiResponse<Void> updateResidentStatus(@PathVariable Long id, @RequestParam String status) {
        propertyService.updateResidentStatus(id, status);
        return ApiResponse.success("住户状态已更新", null);
    }

    @GetMapping("/properties")
    public ApiResponse<Map<String, Object>> getProperties() {
        return ApiResponse.success(propertyService.getProperties());
    }

    @PostMapping("/buildings")
    public ApiResponse<Building> createBuilding(@RequestBody Building building) {
        return ApiResponse.success("楼栋已创建", propertyService.createBuilding(building));
    }

    @PutMapping("/buildings/{id}")
    public ApiResponse<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        return ApiResponse.success("楼栋信息已更新", propertyService.updateBuilding(id, building));
    }

    @PostMapping("/rooms")
    public ApiResponse<Room> createRoom(@RequestBody Room room) {
        return ApiResponse.success("房屋已创建", propertyService.createRoom(room));
    }

    @PutMapping("/rooms/{id}")
    public ApiResponse<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return ApiResponse.success("房屋信息已更新", propertyService.updateRoom(id, room));
    }

    @PatchMapping("/rooms/{id}/resident")
    public ApiResponse<Void> bindRoomResident(
        @PathVariable Long id,
        @RequestParam(required = false) String residentName,
        @RequestParam(required = false) String status
    ) {
        propertyService.bindRoomResident(id, residentName, status);
        return ApiResponse.success("房屋绑定状态已更新", null);
    }

    @GetMapping("/repairs")
    public ApiResponse<List<RepairOrder>> listRepairs() {
        return ApiResponse.success(propertyService.listRepairs());
    }

    @PostMapping("/repairs")
    public ApiResponse<RepairOrder> createRepair(@RequestBody RepairOrder repairOrder) {
        return ApiResponse.success("工单创建成功", propertyService.createRepair(repairOrder));
    }

    @PatchMapping("/repairs/{id}/progress")
    public ApiResponse<Void> updateRepairProgress(@PathVariable Long id, @RequestBody RepairOrder repairOrder) {
        propertyService.updateRepairProgress(id, repairOrder);
        return ApiResponse.success("工单进度已更新", null);
    }

    @GetMapping("/billing")
    public ApiResponse<Map<String, Object>> getBilling() {
        return ApiResponse.success(propertyService.getBilling());
    }

    @PostMapping("/billing")
    public ApiResponse<Bill> createBill(@RequestBody Bill bill) {
        return ApiResponse.success("账单创建成功", propertyService.createBill(bill));
    }

    @PatchMapping("/billing/{id}/status")
    public ApiResponse<Void> updateBillStatus(@PathVariable Long id, @RequestParam String status) {
        propertyService.updateBillStatus(id, status);
        return ApiResponse.success("账单状态已更新", null);
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
    public ApiResponse<Notice> createNotice(@RequestBody Notice notice) {
        return ApiResponse.success("公告发布成功", propertyService.createNotice(notice));
    }

    @GetMapping("/system-users")
    public ApiResponse<List<SysUser>> listSystemUsers() {
        return ApiResponse.success(propertyService.listSystemUsers());
    }

    @PostMapping("/system-users")
    public ApiResponse<SysUser> createSystemUser(@RequestBody SysUser user) {
        return ApiResponse.success("账号创建成功", propertyService.createSystemUser(user));
    }

    @PutMapping("/system-users/{id}")
    public ApiResponse<SysUser> updateSystemUser(@PathVariable Long id, @RequestBody SysUser user) {
        return ApiResponse.success("账号信息已更新", propertyService.updateSystemUser(id, user));
    }

    @PatchMapping("/system-users/{id}/status")
    public ApiResponse<Void> updateSystemUserStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        propertyService.updateSystemUserStatus(id, enabled);
        return ApiResponse.success("账号状态已更新", null);
    }
}
