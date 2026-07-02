package com.training.management.config;

import com.training.management.common.RequestContext;
import com.training.management.common.RoleCodes;
import com.training.management.common.annotation.RequireRole;
import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.SysUser;
import com.training.management.service.AuthService;
import com.training.management.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或登录状态已失效");
        }

        String username;
        try {
            username = jwtUtil.parseUsername(token.substring(7));
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BusinessException(401, "登录状态已失效，请重新登录");
        }

        SysUser user = authService.loadEnabledUser(username);
        RequestContext.setCurrentUser(user);
        checkRole(handler, user);
        return true;
    }

    private void checkRole(Object handler, SysUser user) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }

        RequireRole methodRule = handlerMethod.getMethodAnnotation(RequireRole.class);
        RequireRole classRule = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        RequireRole rule = methodRule != null ? methodRule : classRule;
        if (rule == null || RoleCodes.hasAnyRole(user.getRoleCode(), rule.value())) {
            return;
        }

        throw new BusinessException(403, "当前账号没有执行该操作的权限");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestContext.clear();
    }
}
