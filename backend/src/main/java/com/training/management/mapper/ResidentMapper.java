package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Resident;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ResidentMapper {

    @Select("""
        SELECT id, name, identity_type, phone, building, room_no, vehicles, verified_status, tag, status, created_at
        FROM resident
        ORDER BY id DESC
        """)
    List<Resident> findAll();

    @Select("SELECT COUNT(*) FROM resident")
    long countAll();

    @Select("SELECT COUNT(*) FROM resident WHERE verified_status = '已认证'")
    long countVerified();

    @Insert("""
        INSERT INTO resident (name, identity_type, phone, building, room_no, vehicles, verified_status, tag, status)
        VALUES (#{name}, #{identityType}, #{phone}, #{building}, #{roomNo}, #{vehicles}, #{verifiedStatus}, #{tag}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Resident resident);

    @Update("""
        UPDATE resident
        SET name = #{name},
            identity_type = #{identityType},
            phone = #{phone},
            building = #{building},
            room_no = #{roomNo},
            vehicles = #{vehicles},
            verified_status = #{verifiedStatus},
            tag = #{tag},
            status = #{status}
        WHERE id = #{id}
        """)
    int update(Resident resident);

    @Update("UPDATE resident SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
