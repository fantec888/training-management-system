package com.training.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.domain.entity.Complaint;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.mapper.BillMapper;
import com.training.management.mapper.ComplaintMapper;
import com.training.management.mapper.RepairOrderMapper;
import com.training.management.mapper.ResidentMapper;
import com.training.management.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvancedStatisticsService {

    private final ResidentMapper residentMapper;
    private final RoomMapper roomMapper;
    private final RepairOrderMapper repairOrderMapper;
    private final ComplaintMapper complaintMapper;
    private final BillMapper billMapper;

    public Map<String, Object> getAdvancedStatistics() {
        List<RepairOrder> repairs = repairOrderMapper.findAll();
        List<Complaint> complaints = complaintMapper.findAll();
        BigDecimal receivable = billMapper.sumReceivable();
        BigDecimal paid = billMapper.sumPaid();
        BigDecimal paymentRate = receivable.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : paid.multiply(BigDecimal.valueOf(100)).divide(receivable, 1, RoundingMode.HALF_UP);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("summary", List.of(
            summary("住户总数", String.valueOf(residentMapper.findAll().size())),
            summary("入住率", getOccupancyRate() + "%"),
            summary("缴费率", paymentRate + "%"),
            summary("投诉处理率", getComplaintHandledRate(complaints) + "%")
        ));
        data.put("repairChart", Map.of(
            "pending", repairs.stream().filter(item -> !"已完成".equals(item.getStatus())).count(),
            "finished", repairs.stream().filter(item -> "已完成".equals(item.getStatus())).count(),
            "highPriority", repairs.stream().filter(item -> "高".equals(item.getPriority())).count()
        ));
        data.put("paymentChart", Map.of(
            "receivable", receivable,
            "paid", paid,
            "unpaid", receivable.subtract(paid).max(BigDecimal.ZERO)
        ));
        data.put("complaintChart", Map.of(
            "total", complaints.size(),
            "handled", complaints.stream().filter(item -> item.getReply() != null).count(),
            "pending", complaints.stream().filter(item -> item.getReply() == null).count()
        ));
        return data;
    }

    private BigDecimal getOccupancyRate() {
        long totalRooms = roomMapper.countAll();
        if (totalRooms == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(roomMapper.countOccupied())
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(totalRooms), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal getComplaintHandledRate(List<Complaint> complaints) {
        if (complaints.isEmpty()) {
            return BigDecimal.ZERO;
        }
        long handled = complaints.stream().filter(item -> item.getReply() != null).count();
        return BigDecimal.valueOf(handled)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(complaints.size()), 1, RoundingMode.HALF_UP);
    }

    private Map<String, Object> summary(String label, String value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("label", label);
        item.put("value", value);
        return item;
    }
}
