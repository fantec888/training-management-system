package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.SysPermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysPermissionMapper {

    @Select("""
        SELECT id, permission_code, permission_name, permission_type, parent_id, route_path, component_key,
               sort_order, visible, created_at, updated_at
        FROM sys_permission
        ORDER BY sort_order, id
        """)
    List<SysPermission> findAll();

    @Select("""
        SELECT id, permission_code, permission_name, permission_type, parent_id, route_path, component_key,
               sort_order, visible, created_at, updated_at
        FROM sys_permission
        WHERE permission_code = #{permissionCode}
        LIMIT 1
        """)
    SysPermission findByCode(@Param("permissionCode") String permissionCode);

    @Select("""
        SELECT p.id, p.permission_code, p.permission_name, p.permission_type, p.parent_id, p.route_path,
               p.component_key, p.sort_order, p.visible, p.created_at, p.updated_at
        FROM sys_permission p
        INNER JOIN sys_role_permission rp ON rp.permission_id = p.id
        WHERE rp.role_id = #{roleId}
        ORDER BY p.sort_order, p.id
        """)
    List<SysPermission> findByRoleId(@Param("roleId") Long roleId);

    @Select("""
        SELECT DISTINCT p.permission_code
        FROM sys_permission p
        INNER JOIN sys_role_permission rp ON rp.permission_id = p.id
        INNER JOIN sys_user_role ur ON ur.role_id = rp.role_id
        INNER JOIN sys_role r ON r.id = ur.role_id
        WHERE ur.user_id = #{userId} AND r.enabled = 1
        ORDER BY p.permission_code
        """)
    List<String> findPermissionCodesByUserId(@Param("userId") Long userId);

    @Insert("""
        INSERT INTO sys_permission (permission_code, permission_name, permission_type, parent_id, route_path,
                                    component_key, sort_order, visible)
        VALUES (#{permissionCode}, #{permissionName}, #{permissionType}, #{parentId}, #{routePath},
                #{componentKey}, #{sortOrder}, #{visible})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysPermission permission);

    @Update("""
        UPDATE sys_permission
        SET permission_code = #{permissionCode},
            permission_name = #{permissionName},
            permission_type = #{permissionType},
            parent_id = #{parentId},
            route_path = #{routePath},
            component_key = #{componentKey},
            sort_order = #{sortOrder},
            visible = #{visible},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int update(SysPermission permission);

    @Delete("DELETE FROM sys_permission WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
