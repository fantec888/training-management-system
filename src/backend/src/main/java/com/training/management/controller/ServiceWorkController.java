package com.training.management.controller;

import java.util.List;
import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Complaint;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.service.ServiceWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceWorkController {

    private final ServiceWorkService serviceWorkService;

    @GetMapping("/repairs")
    public ApiResponse<List<RepairOrder>> listRepairs() {
        return ApiResponse.success(serviceWorkService.listRepairs());
    }

    @PostMapping("/repairs")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:repair:create")
    public ApiResponse<RepairOrder> createRepair(@RequestBody RepairOrder repairOrder) {
        return ApiResponse.success("工单创建成功", serviceWorkService.createRepair(repairOrder));
    }

    @PatchMapping("/repairs/{id}/progress")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:repair:update")
    public ApiResponse<Void> updateRepairProgress(@PathVariable Long id, @RequestBody RepairOrder repairOrder) {
        serviceWorkService.updateRepairProgress(id, repairOrder);
        return ApiResponse.success("工单进度已更新", null);
    }

    @DeleteMapping("/repairs/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.ENGINEER_LEAD})
    @RequirePermission("button:repair:delete")
    public ApiResponse<Void> deleteRepair(@PathVariable Long id) {
        serviceWorkService.deleteRepair(id);
        return ApiResponse.success("工单已删除", null);
    }

    @GetMapping("/complaints")
    public ApiResponse<List<Complaint>> listComplaints() {
        return ApiResponse.success(serviceWorkService.listComplaints());
    }

    @PostMapping("/complaints")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    public ApiResponse<Complaint> createComplaint(@RequestBody Complaint complaint) {
        return ApiResponse.success("投诉建议已提交", serviceWorkService.createComplaint(complaint));
    }

    @PatchMapping("/complaints/{id}/reply")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:complaint:handle")
    public ApiResponse<Void> replyComplaint(@PathVariable Long id, @RequestBody Complaint complaint) {
        serviceWorkService.replyComplaint(id, complaint);
        return ApiResponse.success("投诉建议已回复", null);
    }

    @DeleteMapping("/complaints/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:complaint:handle")
    public ApiResponse<Void> deleteComplaint(@PathVariable Long id) {
        serviceWorkService.deleteComplaint(id);
        return ApiResponse.success("投诉建议已删除", null);
    }

    @GetMapping("/notices")
    public ApiResponse<Map<String, Object>> getNotices() {
        return ApiResponse.success(serviceWorkService.getNotices());
    }

    @PostMapping("/notices")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:create")
    public ApiResponse<Notice> createNotice(@RequestBody Notice notice) {
        return ApiResponse.success("公告发布成功", serviceWorkService.createNotice(notice));
    }

    @PatchMapping("/notices/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:create")
    public ApiResponse<Void> updateNoticeStatus(@PathVariable Long id, @RequestParam String status) {
        serviceWorkService.updateNoticeStatus(id, status);
        return ApiResponse.success("公告状态已更新", null);
    }

    @DeleteMapping("/notices/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.SERVICE_MANAGER})
    @RequirePermission("button:notice:delete")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        serviceWorkService.deleteNotice(id);
        return ApiResponse.success("公告已删除", null);
    }
}
