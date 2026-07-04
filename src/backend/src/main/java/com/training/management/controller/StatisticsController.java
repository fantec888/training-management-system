package com.training.management.controller;

import java.util.Map;

import com.training.management.common.ApiResponse;
import com.training.management.service.AdvancedStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final AdvancedStatisticsService advancedStatisticsService;

    @GetMapping("/advanced")
    public ApiResponse<Map<String, Object>> getAdvancedStatistics() {
        return ApiResponse.success(advancedStatisticsService.getAdvancedStatistics());
    }
}
