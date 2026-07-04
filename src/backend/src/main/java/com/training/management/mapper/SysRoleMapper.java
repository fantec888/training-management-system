package com.training.management.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.training.management.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("""
        SELECT id, role_code, role_name, description, enabled, sort_order, created_at, updated_at
        FROM sys_role
        ORDER BY sort_order, id
        """)
    List<SysRole> findAllOrdered();
}
