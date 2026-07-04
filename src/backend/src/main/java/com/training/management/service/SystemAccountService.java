package com.training.management.service;

import java.util.List;

import com.training.management.common.RequestContext;
import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.SysUser;
import com.training.management.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SystemAccountService {

    private final SysUserMapper sysUserMapper;
    private final AuthService authService;

    public List<SysUser> listSystemUsers() {
        return sysUserMapper.findAll();
    }

    public SysUser createSystemUser(SysUser user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new BusinessException(400, "账号不能为空");
        }
        requireText(user.getRealName(), "姓名不能为空");
        requireText(user.getRoleCode(), "角色不能为空");
        if (sysUserMapper.findByUsername(user.getUsername()) != null) {
            throw new BusinessException(400, "账号已存在");
        }
        fillUserDefaults(user);
        if (!StringUtils.hasText(user.getPasswordHash())) {
            user.setPasswordHash(authService.sha256("123456"));
        }
        sysUserMapper.insert(user);
        return user;
    }

    public SysUser updateSystemUser(Long id, SysUser user) {
        requireText(user.getRealName(), "姓名不能为空");
        requireText(user.getRoleCode(), "角色不能为空");
        user.setId(id);
        fillUserDefaults(user);
        sysUserMapper.update(user);
        return user;
    }

    public void updateSystemUserStatus(Long id, Boolean enabled) {
        if (Boolean.FALSE.equals(enabled)) {
            preventCurrentUserRemoval(id, "不能冻结当前登录账号");
        }
        sysUserMapper.updateEnabled(id, enabled);
    }

    public void deleteSystemUser(Long id) {
        preventCurrentUserRemoval(id, "不能删除当前登录账号");
        sysUserMapper.deleteById(id);
    }

    private void fillUserDefaults(SysUser user) {
        if (!StringUtils.hasText(user.getRoleCode())) {
            user.setRoleCode("SERVICE_MANAGER");
        }
        if (!StringUtils.hasText(user.getPermissionScope())) {
            user.setPermissionScope("住户、工单、公告");
        }
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
    }

    private void preventCurrentUserRemoval(Long id, String message) {
        SysUser currentUser = RequestContext.getCurrentUser();
        if (currentUser != null && id != null && id.equals(currentUser.getId())) {
            throw new BusinessException(400, message);
        }
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(400, message);
        }
    }
}
