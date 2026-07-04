package com.training.management.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRolePermissionMapper {

    @Select("""
        SELECT p.permission_code
        FROM sys_permission p
        INNER JOIN sys_role_permission rp ON rp.permission_id = p.id
        WHERE rp.role_id = #{roleId}
        ORDER BY p.sort_order, p.id
        """)
    List<String> findPermissionCodesByRoleId(@Param("roleId") Long roleId);

    @Select("""
        SELECT id
        FROM sys_permission
        WHERE permission_code = #{permissionCode}
        LIMIT 1
        """)
    Long findPermissionIdByCode(@Param("permissionCode") String permissionCode);

    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM sys_role_permission WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(@Param("permissionId") Long permissionId);

    @Insert("INSERT INTO sys_role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    int insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
