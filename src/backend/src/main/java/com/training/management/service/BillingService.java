package com.training.management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.FeeItem;
import com.training.management.domain.entity.PaymentRecord;
import com.training.management.mapper.BillMapper;
import com.training.management.mapper.FeeItemMapper;
import com.training.management.mapper.PaymentRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillMapper billMapper;
    private final FeeItemMapper feeItemMapper;
    private final PaymentRecordMapper paymentRecordMapper;

    public Map<String, Object> getBilling() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("summary", Map.of(
            "receivable", billMapper.sumReceivable(),
            "paid", billMapper.sumPaid(),
            "unpaidCount", billMapper.countUnpaid(),
            "paidCount", billMapper.countByStatus("已缴费"),
            "overdueCount", billMapper.countByStatus("逾期")
        ));
        data.put("bills", billMapper.findAll());
        data.put("feeItems", feeItemMapper.findAll());
        data.put("payments", paymentRecordMapper.findAll());
        return data;
    }

    public Bill createBill(Bill bill) {
        requireText(bill.getResidentName(), "缴费住户不能为空");
        requireText(bill.getHouse(), "房号不能为空");
        requireText(bill.getFeeType(), "费用类型不能为空");
        if (!StringUtils.hasText(bill.getStatus())) {
            bill.setStatus("待缴费");
        }
        if (!StringUtils.hasText(bill.getBillPeriod())) {
            bill.setBillPeriod(DateTimeFormatter.ofPattern("yyyy-MM").format(LocalDate.now()));
        }
        if (bill.getDueDate() == null) {
            bill.setDueDate(LocalDate.now().plusDays(10));
        }
        if (bill.getAmount() == null) {
            bill.setAmount(BigDecimal.ZERO);
        }
        billMapper.insert(bill);
        return bill;
    }

    public void updateBillStatus(Long id, String status) {
        requireText(status, "账单状态不能为空");
        billMapper.updateStatus(id, status);
    }

    public void deleteBill(Long id) {
        billMapper.deleteById(id);
    }

    public List<FeeItem> listFeeItems() {
        return feeItemMapper.findAll();
    }

    public FeeItem createFeeItem(FeeItem feeItem) {
        requireText(feeItem.getName(), "费用项目名称不能为空");
        fillFeeItemDefaults(feeItem);
        feeItemMapper.insert(feeItem);
        return feeItem;
    }

    public FeeItem updateFeeItem(Long id, FeeItem feeItem) {
        requireText(feeItem.getName(), "费用项目名称不能为空");
        feeItem.setId(id);
        fillFeeItemDefaults(feeItem);
        feeItemMapper.update(feeItem);
        return feeItem;
    }

    public void deleteFeeItem(Long id) {
        feeItemMapper.deleteById(id);
    }

    public PaymentRecord createPayment(PaymentRecord paymentRecord) {
        requireText(paymentRecord.getPayerName(), "缴费人不能为空");
        requireText(paymentRecord.getHouse(), "房号不能为空");
        if (paymentRecord.getPaidAt() == null) {
            paymentRecord.setPaidAt(LocalDateTime.now());
        }
        if (!StringUtils.hasText(paymentRecord.getPaymentMethod())) {
            paymentRecord.setPaymentMethod("微信支付");
        }
        if (!StringUtils.hasText(paymentRecord.getOperator())) {
            paymentRecord.setOperator("财务专员");
        }
        paymentRecordMapper.insert(paymentRecord);
        if (paymentRecord.getBillId() != null) {
            billMapper.updateStatus(paymentRecord.getBillId(), "已缴费");
        }
        return paymentRecord;
    }

    private void fillFeeItemDefaults(FeeItem feeItem) {
        if (!StringUtils.hasText(feeItem.getCategory())) {
            feeItem.setCategory("物业费");
        }
        if (!StringUtils.hasText(feeItem.getBillingCycle())) {
            feeItem.setBillingCycle("月度");
        }
        if (feeItem.getUnitPrice() == null) {
            feeItem.setUnitPrice(BigDecimal.ZERO);
        }
        if (feeItem.getEnabled() == null) {
            feeItem.setEnabled(true);
        }
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(400, message);
        }
    }
}
