package com.training.management.mapper;

import com.training.management.domain.entity.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysUserMapper {

    @Select("""
        SELECT id, username, password_hash, real_name, role_code, department, permission_scope, phone, enabled, last_login_at, created_at, updated_at
        FROM sys_user
        WHERE username = #{username}
        LIMIT 1
        """)
    SysUser findByUsername(@Param("username") String username);

    @Select("""
        SELECT id, username, password_hash, real_name, role_code, department, permission_scope, phone, enabled, last_login_at, created_at, updated_at
        FROM sys_user
        WHERE id = #{id}
        LIMIT 1
        """)
    SysUser findById(@Param("id") Long id);

    @Select("""
        SELECT id, username, password_hash, real_name, role_code, department, permission_scope, phone, enabled, last_login_at, created_at, updated_at
        FROM sys_user
        ORDER BY id
        """)
    java.util.List<SysUser> findAll();

    @Insert("""
        INSERT INTO sys_user (username, password_hash, real_name, role_code, department, permission_scope, phone, enabled)
        VALUES (#{username}, #{passwordHash}, #{realName}, #{roleCode}, #{department}, #{permissionScope}, #{phone}, #{enabled})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    @Update("""
        UPDATE sys_user
        SET enabled = #{enabled}, updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int updateEnabled(@Param("id") Long id, @Param("enabled") Boolean enabled);

    @Update("""
        UPDATE sys_user
        SET real_name = #{realName},
            role_code = #{roleCode},
            department = #{department},
            permission_scope = #{permissionScope},
            phone = #{phone},
            enabled = #{enabled},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int update(SysUser user);

    @Update("""
        UPDATE sys_user
        SET role_code = #{roleCode}, updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int updateRoleCode(@Param("id") Long id, @Param("roleCode") String roleCode);

    @Update("""
        UPDATE sys_user
        SET last_login_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int updateLastLogin(@Param("id") Long id);

    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
