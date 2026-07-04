package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.FeeItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FeeItemMapper {

    @Select("""
        SELECT id, name, category, billing_cycle, unit_price, description, enabled, created_at
        FROM fee_item
        ORDER BY id DESC
        """)
    List<FeeItem> findAll();

    @Insert("""
        INSERT INTO fee_item (name, category, billing_cycle, unit_price, description, enabled)
        VALUES (#{name}, #{category}, #{billingCycle}, #{unitPrice}, #{description}, #{enabled})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FeeItem feeItem);

    @Update("""
        UPDATE fee_item
        SET name = #{name},
            category = #{category},
            billing_cycle = #{billingCycle},
            unit_price = #{unitPrice},
            description = #{description},
            enabled = #{enabled}
        WHERE id = #{id}
        """)
    int update(FeeItem feeItem);

    @Delete("DELETE FROM fee_item WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
