package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.CommunityActivity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommunityActivityMapper {

    @Select("""
        SELECT id, title, activity_type, location, organizer, start_time, signups, status, created_at
        FROM community_activity
        ORDER BY start_time DESC, id DESC
        """)
    List<CommunityActivity> findAll();

    @Insert("""
        INSERT INTO community_activity (title, activity_type, location, organizer, start_time, signups, status)
        VALUES (#{title}, #{activityType}, #{location}, #{organizer}, #{startTime}, #{signups}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CommunityActivity activity);

    @Update("""
        UPDATE community_activity
        SET title = #{title}, activity_type = #{activityType}, location = #{location},
            organizer = #{organizer}, start_time = #{startTime}, signups = #{signups}, status = #{status}
        WHERE id = #{id}
        """)
    int update(CommunityActivity activity);

    @Update("UPDATE community_activity SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM community_activity WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
