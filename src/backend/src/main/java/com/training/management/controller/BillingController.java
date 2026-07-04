package com.training.management.controller;

import java.util.List;
import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequirePermission;
import com.training.management.common.annotation.RequireRole;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.FeeItem;
import com.training.management.domain.entity.PaymentRecord;
import com.training.management.service.BillingService;
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
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/billing")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    public ApiResponse<Map<String, Object>> getBilling() {
        return ApiResponse.success(billingService.getBilling());
    }

    @PostMapping("/billing")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:create")
    public ApiResponse<Bill> createBill(@RequestBody Bill bill) {
        return ApiResponse.success("账单创建成功", billingService.createBill(bill));
    }

    @PatchMapping("/billing/{id}/status")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:update")
    public ApiResponse<Void> updateBillStatus(@PathVariable Long id, @RequestParam String status) {
        billingService.updateBillStatus(id, status);
        return ApiResponse.success("账单状态已更新", null);
    }

    @DeleteMapping("/billing/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:bill:delete")
    public ApiResponse<Void> deleteBill(@PathVariable Long id) {
        billingService.deleteBill(id);
        return ApiResponse.success("账单已删除", null);
    }

    @GetMapping("/fee-items")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    public ApiResponse<List<FeeItem>> listFeeItems() {
        return ApiResponse.success(billingService.listFeeItems());
    }

    @PostMapping("/fee-items")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:fee-item:manage")
    public ApiResponse<FeeItem> createFeeItem(@RequestBody FeeItem feeItem) {
        return ApiResponse.success("费用项目已创建", billingService.createFeeItem(feeItem));
    }

    @PutMapping("/fee-items/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:fee-item:manage")
    public ApiResponse<FeeItem> updateFeeItem(@PathVariable Long id, @RequestBody FeeItem feeItem) {
        return ApiResponse.success("费用项目已更新", billingService.updateFeeItem(id, feeItem));
    }

    @DeleteMapping("/fee-items/{id}")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:fee-item:manage")
    public ApiResponse<Void> deleteFeeItem(@PathVariable Long id) {
        billingService.deleteFeeItem(id);
        return ApiResponse.success("费用项目已删除", null);
    }

    @PostMapping("/payments")
    @RequireRole({RoleCodes.SUPER_ADMIN, RoleCodes.FINANCE_ADMIN})
    @RequirePermission("button:payment:create")
    public ApiResponse<PaymentRecord> createPayment(@RequestBody PaymentRecord paymentRecord) {
        return ApiResponse.success("缴费登记成功", billingService.createPayment(paymentRecord));
    }
}
