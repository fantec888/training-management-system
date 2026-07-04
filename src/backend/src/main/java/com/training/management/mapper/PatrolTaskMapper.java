package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.PatrolTask;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PatrolTaskMapper {

    @Select("""
        SELECT id, route_name, area, assignee, plan_time, status, result, finished_at
        FROM patrol_task
        ORDER BY plan_time DESC, id DESC
        """)
    List<PatrolTask> findAll();

    @Insert("""
        INSERT INTO patrol_task (route_name, area, assignee, plan_time, status, result, finished_at)
        VALUES (#{routeName}, #{area}, #{assignee}, #{planTime}, #{status}, #{result}, #{finishedAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PatrolTask patrolTask);

    @Update("""
        UPDATE patrol_task
        SET route_name = #{routeName}, area = #{area}, assignee = #{assignee},
            plan_time = #{planTime}, status = #{status}, result = #{result}, finished_at = #{finishedAt}
        WHERE id = #{id}
        """)
    int update(PatrolTask patrolTask);

    @Update("UPDATE patrol_task SET status = #{status}, result = #{result}, finished_at = #{finishedAt} WHERE id = #{id}")
    int updateResult(PatrolTask patrolTask);

    @Delete("DELETE FROM patrol_task WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
