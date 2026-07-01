package com.training.management.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.training.management.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "ok");
        result.put("message", "后端服务启动成功");
        result.put("time", LocalDateTime.now().toString());
        return ApiResponse.success(result);
    }
}
