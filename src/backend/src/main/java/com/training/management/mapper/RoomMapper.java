package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

    @Select("""
        SELECT id, building_name, unit_no, room_no, floor_no, area, resident_name, status, updated_at
        FROM room
        ORDER BY building_name, unit_no, room_no
        """)
    List<Room> findAll();

    @Select("SELECT COUNT(*) FROM room")
    long countAll();

    @Select("SELECT COUNT(*) FROM room WHERE status = '空置'")
    long countVacant();

    @Select("SELECT COUNT(*) FROM room WHERE status = '已入住'")
    long countOccupied();

    @Select("SELECT COUNT(*) FROM room WHERE status = '装修中'")
    long countDecorating();

    @Insert("""
        INSERT INTO room (building_name, unit_no, room_no, floor_no, area, resident_name, status)
        VALUES (#{buildingName}, #{unitNo}, #{roomNo}, #{floorNo}, #{area}, #{residentName}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);

    @Update("""
        UPDATE room
        SET building_name = #{buildingName},
            unit_no = #{unitNo},
            room_no = #{roomNo},
            floor_no = #{floorNo},
            area = #{area},
            resident_name = #{residentName},
            status = #{status},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int update(Room room);

    @Update("""
        UPDATE room
        SET resident_name = #{residentName},
            status = #{status},
            updated_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int bindResident(@Param("id") Long id, @Param("residentName") String residentName, @Param("status") String status);
}
