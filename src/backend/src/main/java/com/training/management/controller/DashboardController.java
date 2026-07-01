package com.training.management.controller;

import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getDashboard() {
        return ApiResponse.success(dashboardService.getDashboard());
    }
}
