package com.training.management.controller;

import com.training.management.common.ApiResponse;
import com.training.management.common.RequestContext;
import com.training.management.domain.dto.LoginRequest;
import com.training.management.domain.vo.LoginUserVO;
import com.training.management.domain.vo.LoginVO;
import com.training.management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("登录成功", authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<LoginUserVO> me() {
        return ApiResponse.success(authService.buildLoginUser(RequestContext.getCurrentUser()));
    }
}
