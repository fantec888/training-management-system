package com.training.management.mapper;

import java.util.List;

import com.training.management.domain.entity.PaymentRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentRecordMapper {

    @Select("""
        SELECT id, bill_id, payer_name, house, amount, payment_method, operator, remark, paid_at
        FROM payment_record
        ORDER BY paid_at DESC, id DESC
        """)
    List<PaymentRecord> findAll();

    @Insert("""
        INSERT INTO payment_record (bill_id, payer_name, house, amount, payment_method, operator, remark, paid_at)
        VALUES (#{billId}, #{payerName}, #{house}, #{amount}, #{paymentMethod}, #{operator}, #{remark}, #{paidAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PaymentRecord paymentRecord);
}
