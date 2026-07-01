package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RepairOrderMapper {

    @Select("""
        SELECT id, code, title, area, priority, assignee, status, duration_hours, created_at
        FROM repair_order
        ORDER BY created_at DESC, id DESC
        """)
    List<RepairOrder> findAll();

    @Select("SELECT COUNT(*) FROM repair_order WHERE status <> '已完成'")
    long countPending();

    @Select("SELECT COUNT(*) FROM repair_order WHERE status = #{status}")
    long countByStatus(@Param("status") String status);

    @Select("SELECT priority AS label, COUNT(*) AS item_count FROM repair_order GROUP BY priority ORDER BY COUNT(*) DESC")
    List<java.util.Map<String, Object>> countByPriority();

    @Insert("""
        INSERT INTO repair_order (code, title, area, priority, assignee, status, duration_hours, created_at)
        VALUES (#{code}, #{title}, #{area}, #{priority}, #{assignee}, #{status}, #{durationHours}, CURRENT_TIMESTAMP)
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RepairOrder repairOrder);

    @Update("""
        UPDATE repair_order
        SET status = #{status},
            assignee = #{assignee},
            duration_hours = #{durationHours}
        WHERE id = #{id}
        """)
    int updateProgress(
        @Param("id") Long id,
        @Param("status") String status,
        @Param("assignee") String assignee,
        @Param("durationHours") java.math.BigDecimal durationHours
    );

    @Delete("DELETE FROM repair_order WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
