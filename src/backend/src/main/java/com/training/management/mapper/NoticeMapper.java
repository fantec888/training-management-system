package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoticeMapper {

    @Select("""
        SELECT id, title, category, audience, publisher, publish_time, status
        FROM notice
        ORDER BY publish_time DESC, id DESC
        """)
    List<Notice> findAll();

    @Select("SELECT COUNT(*) FROM notice")
    long countAll();

    @Select("SELECT COUNT(*) FROM notice WHERE category = '活动'")
    long countActivities();

    @Insert("""
        INSERT INTO notice (title, category, audience, publisher, publish_time, status)
        VALUES (#{title}, #{category}, #{audience}, #{publisher}, #{publishTime}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notice notice);
}
