package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.ExpressPackage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ExpressPackageMapper {

    @Select("""
        SELECT id, tracking_no, recipient_name, phone, pickup_code, cabinet_no, status, arrived_at, picked_at
        FROM express_package
        ORDER BY arrived_at DESC, id DESC
        """)
    List<ExpressPackage> findAll();

    @Insert("""
        INSERT INTO express_package (tracking_no, recipient_name, phone, pickup_code, cabinet_no, status, arrived_at, picked_at)
        VALUES (#{trackingNo}, #{recipientName}, #{phone}, #{pickupCode}, #{cabinetNo}, #{status}, #{arrivedAt}, #{pickedAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ExpressPackage expressPackage);

    @Update("""
        UPDATE express_package
        SET tracking_no = #{trackingNo}, recipient_name = #{recipientName}, phone = #{phone},
            pickup_code = #{pickupCode}, cabinet_no = #{cabinetNo}, status = #{status}, picked_at = #{pickedAt}
        WHERE id = #{id}
        """)
    int update(ExpressPackage expressPackage);

    @Update("UPDATE express_package SET status = #{status}, picked_at = #{pickedAt} WHERE id = #{id}")
    int updateStatus(ExpressPackage expressPackage);

    @Delete("DELETE FROM express_package WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
