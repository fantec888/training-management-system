package com.training.management.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.training.management.common.PageResult;
import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.SysPermission;
import com.training.management.domain.entity.SysRole;
import com.training.management.domain.entity.SysUser;
import com.training.management.mapper.ResidentMapper;
import com.training.management.mapper.SysPermissionMapper;
import com.training.management.mapper.SysRoleMapper;
import com.training.management.mapper.SysRolePermissionMapper;
import com.training.management.mapper.SysUserMapper;
import com.training.management.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RbacService {

    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;
    private final SysUserMapper sysUserMapper;
    private final ResidentMapper residentMapper;

    public List<SysRole> listRoles() {
        return sysRoleMapper.findAllOrdered();
    }

    public SysRole createRole(SysRole role) {
        validateRole(role);
        role.setEnabled(role.getEnabled() == null || role.getEnabled());
        role.setSortOrder(role.getSortOrder() == null ? 99 : role.getSortOrder());
        sysRoleMapper.insert(role);
        return role;
    }

    public SysRole updateRole(Long id, SysRole role) {
        validateRole(role);
        role.setId(id);
        role.setEnabled(role.getEnabled() == null || role.getEnabled());
        role.setSortOrder(role.getSortOrder() == null ? 99 : role.getSortOrder());
        sysRoleMapper.updateById(role);
        return role;
    }

    @Transactional
    public void deleteRole(Long id) {
        sysRolePermissionMapper.deleteByRoleId(id);
        sysUserRoleMapper.deleteByRoleId(id);
        sysRoleMapper.deleteById(id);
    }

    public List<SysPermission> listPermissions() {
        return sysPermissionMapper.findAll();
    }

    public SysPermission createPermission(SysPermission permission) {
        validatePermission(permission);
        fillPermissionDefaults(permission);
        sysPermissionMapper.insert(permission);
        return permission;
    }

    public SysPermission updatePermission(Long id, SysPermission permission) {
        validatePermission(permission);
        fillPermissionDefaults(permission);
        permission.setId(id);
        sysPermissionMapper.update(permission);
        return permission;
    }

    @Transactional
    public void deletePermission(Long id) {
        sysRolePermissionMapper.deleteByPermissionId(id);
        sysPermissionMapper.deleteById(id);
    }

    public List<String> listUserRoleCodes(Long userId) {
        return sysUserRoleMapper.findRoleCodesByUserId(userId);
    }

    @Transactional
    public void assignUserRoles(Long userId, List<String> roleCodes) {
        SysUser user = sysUserMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        List<String> cleanRoleCodes = normalizeCodes(roleCodes);
        if (cleanRoleCodes.isEmpty()) {
            throw new BusinessException(400, "至少选择一个角色");
        }

        sysUserRoleMapper.deleteByUserId(userId);
        for (String roleCode : cleanRoleCodes) {
            Long roleId = sysUserRoleMapper.findRoleIdByCode(roleCode);
            if (roleId == null) {
                throw new BusinessException(400, "角色不存在：" + roleCode);
            }
            sysUserRoleMapper.insert(userId, roleId);
        }
        sysUserMapper.updateRoleCode(userId, cleanRoleCodes.get(0));
    }

    public List<String> listRolePermissionCodes(Long roleId) {
        return sysRolePermissionMapper.findPermissionCodesByRoleId(roleId);
    }

    @Transactional
    public void assignRolePermissions(Long roleId, List<String> permissionCodes) {
        if (sysRoleMapper.selectById(roleId) == null) {
            throw new BusinessException(404, "角色不存在");
        }
        sysRolePermissionMapper.deleteByRoleId(roleId);
        for (String permissionCode : normalizeCodes(permissionCodes)) {
            Long permissionId = sysRolePermissionMapper.findPermissionIdByCode(permissionCode);
            if (permissionId == null) {
                throw new BusinessException(400, "权限不存在：" + permissionCode);
            }
            sysRolePermissionMapper.insert(roleId, permissionId);
        }
    }

    public Map<String, Object> buildCurrentPermission(SysUser user) {
        List<String> roleCodes = listUserRoleCodes(user.getId());
        if (roleCodes.isEmpty() && StringUtils.hasText(user.getRoleCode())) {
            roleCodes = List.of(user.getRoleCode());
        }
        List<String> permissionCodes = listPermissionCodesByUserId(user.getId());
        return buildPermissionPayload(roleCodes, permissionCodes);
    }

    public List<String> listPermissionCodesByUserId(Long userId) {
        return sysPermissionMapper.findPermissionCodesByUserId(userId);
    }

    public boolean hasAnyPermission(Long userId, String[] permissionCodes) {
        if (permissionCodes == null || permissionCodes.length == 0) {
            return true;
        }
        Set<String> userPermissions = new LinkedHashSet<>(listPermissionCodesByUserId(userId));
        for (String permissionCode : permissionCodes) {
            if (userPermissions.contains(permissionCode)) {
                return true;
            }
        }
        return false;
    }

    public PageResult<Resident> pageResidents(int pageNum, int pageSize) {
        com.github.pagehelper.PageHelper.startPage(Math.max(pageNum, 1), Math.max(pageSize, 1));
        List<Resident> rows = residentMapper.findAll();
        PageInfo<Resident> page = new PageInfo<>(rows);
        return new PageResult<>(
            page.getList(),
            page.getTotal(),
            page.getPageNum(),
            page.getPageSize(),
            page.getPages()
        );
    }

    private Map<String, Object> buildPermissionPayload(List<String> roleCodes, List<String> permissionCodes) {
        List<String> menus = permissionCodes.stream()
            .filter(code -> code.startsWith("menu:"))
            .map(code -> code.substring("menu:".length()))
            .toList();
        List<String> buttons = permissionCodes.stream()
            .filter(code -> code.startsWith("button:"))
            .toList();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("roleCodes", roleCodes);
        data.put("permissions", permissionCodes);
        data.put("menus", menus);
        data.put("buttons", buttons);
        return data;
    }

    private void validateRole(SysRole role) {
        if (!StringUtils.hasText(role.getRoleCode())) {
            throw new BusinessException(400, "角色编码不能为空");
        }
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new BusinessException(400, "角色名称不能为空");
        }
    }

    private void validatePermission(SysPermission permission) {
        if (!StringUtils.hasText(permission.getPermissionCode())) {
            throw new BusinessException(400, "权限编码不能为空");
        }
        if (!StringUtils.hasText(permission.getPermissionName())) {
            throw new BusinessException(400, "权限名称不能为空");
        }
        if (!StringUtils.hasText(permission.getPermissionType())) {
            throw new BusinessException(400, "权限类型不能为空");
        }
    }

    private void fillPermissionDefaults(SysPermission permission) {
        permission.setPermissionType(permission.getPermissionType().toUpperCase());
        permission.setSortOrder(permission.getSortOrder() == null ? 99 : permission.getSortOrder());
        permission.setVisible(permission.getVisible() == null || permission.getVisible());
    }

    private List<String> normalizeCodes(List<String> codes) {
        if (codes == null) {
            return new ArrayList<>();
        }
        return codes.stream()
            .filter(StringUtils::hasText)
            .map(String::trim)
            .distinct()
            .toList();
    }
}
