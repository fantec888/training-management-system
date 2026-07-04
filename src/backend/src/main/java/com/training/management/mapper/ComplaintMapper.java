package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.Complaint;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ComplaintMapper {

    @Select("""
        SELECT id, code, resident_name, phone, category, title, content, handler, reply, status, submitted_at, replied_at
        FROM complaint
        ORDER BY submitted_at DESC, id DESC
        """)
    List<Complaint> findAll();

    @Insert("""
        INSERT INTO complaint (code, resident_name, phone, category, title, content, handler, reply, status, submitted_at, replied_at)
        VALUES (#{code}, #{residentName}, #{phone}, #{category}, #{title}, #{content}, #{handler}, #{reply}, #{status}, #{submittedAt}, #{repliedAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Complaint complaint);

    @Update("""
        UPDATE complaint
        SET handler = #{handler},
            reply = #{reply},
            status = #{status},
            replied_at = CURRENT_TIMESTAMP
        WHERE id = #{id}
        """)
    int updateReply(Complaint complaint);

    @Delete("DELETE FROM complaint WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
