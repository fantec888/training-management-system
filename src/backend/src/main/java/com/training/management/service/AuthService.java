package com.training.management.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.dto.LoginRequest;
import com.training.management.domain.entity.SysUser;
import com.training.management.domain.vo.LoginUserVO;
import com.training.management.domain.vo.LoginVO;
import com.training.management.mapper.SysUserMapper;
import com.training.management.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;
    private final RbacService rbacService;

    public LoginVO login(LoginRequest request) {
        SysUser user = loadEnabledUser(request.getUsername());
        String passwordHash = sha256(request.getPassword());
        if (!passwordHash.equalsIgnoreCase(user.getPasswordHash())) {
            throw new BusinessException(401, "账号或密码不正确");
        }

        sysUserMapper.updateLastLogin(user.getId());
        return new LoginVO(jwtUtil.generateToken(user), buildLoginUser(user));
    }

    public SysUser loadEnabledUser(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BusinessException(401, "登录状态无效");
        }

        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(401, "账号不存在");
        }
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new BusinessException(403, "账号已被停用");
        }
        return user;
    }

    public LoginUserVO buildLoginUser(SysUser user) {
        Map<String, Object> permissions = rbacService.buildCurrentPermission(user);
        return new LoginUserVO(
            user.getId(),
            user.getUsername(),
            user.getRealName(),
            user.getRoleCode(),
            user.getPhone(),
            castList(permissions.get("roleCodes")),
            castList(permissions.get("permissions")),
            castList(permissions.get("menus")),
            castList(permissions.get("buttons"))
        );
    }

    @SuppressWarnings("unchecked")
    private List<String> castList(Object value) {
        return value instanceof List<?> ? (List<String>) value : List.of();
    }

    public String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte current : hash) {
                builder.append(String.format("%02x", current));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", exception);
        }
    }
}
