package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.AccessControl;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccessControlMapper {

    @Select("""
        SELECT id, device_name, gate_name, device_type, status, manager, last_check_at
        FROM access_control
        ORDER BY id DESC
        """)
    List<AccessControl> findAll();

    @Insert("""
        INSERT INTO access_control (device_name, gate_name, device_type, status, manager, last_check_at)
        VALUES (#{deviceName}, #{gateName}, #{deviceType}, #{status}, #{manager}, #{lastCheckAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AccessControl accessControl);

    @Update("""
        UPDATE access_control
        SET device_name = #{deviceName}, gate_name = #{gateName}, device_type = #{deviceType},
            status = #{status}, manager = #{manager}, last_check_at = #{lastCheckAt}
        WHERE id = #{id}
        """)
    int update(AccessControl accessControl);

    @Update("UPDATE access_control SET status = #{status}, last_check_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM access_control WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
