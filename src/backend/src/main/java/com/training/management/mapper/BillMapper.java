package com.training.management.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.training.management.domain.entity.Bill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BillMapper {

    @Select("""
        SELECT id, resident_name, house, fee_type, bill_period, amount, status, due_date
        FROM bill
        ORDER BY due_date DESC, id DESC
        """)
    List<Bill> findAll();

    @Select("SELECT COALESCE(SUM(amount), 0) FROM bill")
    BigDecimal sumReceivable();

    @Select("SELECT COALESCE(SUM(amount), 0) FROM bill WHERE status = '已缴费'")
    BigDecimal sumPaid();

    @Select("SELECT COUNT(*) FROM bill WHERE status <> '已缴费'")
    long countUnpaid();

    @Select("SELECT COUNT(*) FROM bill WHERE status = #{status}")
    long countByStatus(@Param("status") String status);

    @Insert("""
        INSERT INTO bill (resident_name, house, fee_type, bill_period, amount, status, due_date)
        VALUES (#{residentName}, #{house}, #{feeType}, #{billPeriod}, #{amount}, #{status}, #{dueDate})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Bill bill);

    @Update("UPDATE bill SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Delete("DELETE FROM bill WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
