package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Community;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommunityMapper {

    @Select("""
        SELECT id, name, address, developer, property_company, total_buildings, total_rooms,
               manager, status, created_at, updated_at
        FROM community
        ORDER BY id DESC
        """)
    List<Community> findAll();

    @Insert("""
        INSERT INTO community (name, address, developer, property_company, total_buildings, total_rooms, manager, status)
        VALUES (#{name}, #{address}, #{developer}, #{propertyCompany}, #{totalBuildings}, #{totalRooms}, #{manager}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Community community);

    @Update("""
        UPDATE community
        SET name = #{name},
            address = #{address},
            developer = #{developer},
            property_company = #{propertyCompany},
            total_buildings = #{totalBuildings},
            total_rooms = #{totalRooms},
            manager = #{manager},
            status = #{status},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int update(Community community);

    @Delete("DELETE FROM community WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
