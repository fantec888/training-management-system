package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Building;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BuildingMapper {

    @Select("""
        SELECT id, name, floors, units, occupancy_rate, vacant_count, manager, status
        FROM building
        ORDER BY id
        """)
    List<Building> findAll();

    @Select("SELECT COUNT(*) FROM building")
    long countAll();

    @Select("SELECT COALESCE(SUM(vacant_count), 0) FROM building")
    long countVacant();

    @Select("SELECT COALESCE(AVG(occupancy_rate), 0) FROM building")
    java.math.BigDecimal averageOccupancyRate();
    @Insert("""
        INSERT INTO building (name, floors, units, occupancy_rate, vacant_count, manager, status)
        VALUES (#{name}, #{floors}, #{units}, #{occupancyRate}, #{vacantCount}, #{manager}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Building building);

    @Update("""
        UPDATE building
        SET name = #{name},
            floors = #{floors},
            units = #{units},
            occupancy_rate = #{occupancyRate},
            vacant_count = #{vacantCount},
            manager = #{manager},
            status = #{status}
        WHERE id = #{id}
        """)
    int update(Building building);
}
