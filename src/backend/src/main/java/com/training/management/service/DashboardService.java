package com.training.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.mapper.BillMapper;
import com.training.management.mapper.DashboardMapper;
import com.training.management.mapper.NoticeMapper;
import com.training.management.mapper.ParkingVehicleMapper;
import com.training.management.mapper.RepairOrderMapper;
import com.training.management.mapper.ResidentMapper;
import com.training.management.mapper.RoomMapper;
import com.training.management.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ResidentMapper residentMapper;
    private final RepairOrderMapper repairOrderMapper;
    private final BillMapper billMapper;
    private final ParkingVehicleMapper parkingVehicleMapper;
    private final NoticeMapper noticeMapper;
    private final DashboardMapper dashboardMapper;
    private final RoomMapper roomMapper;
    private final SysUserMapper sysUserMapper;

    public Map<String, Object> getDashboard() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stats", buildStats());
        data.put("revenueTrend", dashboardMapper.findMonthlyRevenue());
        data.put("workOrderTrend", buildWorkOrderTrend());
        data.put("todoList", dashboardMapper.findTodoItems());
        data.put("announcements", noticeMapper.findAll().stream().limit(4).toList());
        data.put("moduleHealth", buildModuleHealth());
        return data;
    }

    private List<Map<String, Object>> buildWorkOrderTrend() {
        return repairOrderMapper.countByPriority().stream()
            .map(item -> {
                Map<String, Object> normalized = new LinkedHashMap<>();
                normalized.put("label", getMapValue(item, "LABEL", "label"));
                normalized.put("value", getMapValue(item, "ITEM_COUNT", "item_count"));
                return normalized;
            })
            .toList();
    }

    private List<Map<String, Object>> buildStats() {
        long residents = residentMapper.countAll();
        long rooms = roomMapper.countAll();
        long occupiedRooms = roomMapper.countOccupied();
        BigDecimal occupancyRate = rooms == 0
            ? BigDecimal.ZERO
            : BigDecimal.valueOf(occupiedRooms)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(rooms), 1, RoundingMode.HALF_UP);

        BigDecimal paid = billMapper.sumPaid();
        BigDecimal receivable = billMapper.sumReceivable();
        BigDecimal collectionRate = receivable.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : paid.multiply(BigDecimal.valueOf(100)).divide(receivable, 1, RoundingMode.HALF_UP);

        long fixedVehicles = parkingVehicleMapper.countFixed();
        long temporaryVehicles = parkingVehicleMapper.countTemporary();
        long parkingTotal = fixedVehicles + temporaryVehicles + 124;
        BigDecimal parkingRate = parkingTotal == 0
            ? BigDecimal.ZERO
            : BigDecimal.valueOf(fixedVehicles + temporaryVehicles)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(parkingTotal), 1, RoundingMode.HALF_UP);

        return List.of(
            stat("住户总数", String.valueOf(residents), "实时", "住户管理新增后会同步变化"),
            stat("房屋入住率", occupancyRate + "%", occupiedRooms + "/" + rooms, "房屋绑定住户后会同步变化"),
            stat("收缴率", collectionRate + "%", "已收 " + paid + " 元", "账单缴费确认后会同步变化"),
            stat("待处理工单", String.valueOf(repairOrderMapper.countPending()), "处理中 " + repairOrderMapper.countByStatus("处理中"), "工单完成后会同步减少"),
            stat("空置房屋", String.valueOf(roomMapper.countVacant()), "装修中 " + roomMapper.countDecorating(), "房屋状态维护后会同步变化"),
            stat("停车使用率", parkingRate + "%", "车辆 " + (fixedVehicles + temporaryVehicles), "停车数据来自停车管理模块")
        );
    }

    private List<Map<String, Object>> buildModuleHealth() {
        return List.of(
            health("住户管理", residentMapper.countAll(), "住户档案和认证状态"),
            health("楼栋房屋", roomMapper.countAll(), "楼栋、房屋、入住状态"),
            health("收费管理", billMapper.findAll().size(), "账单和缴费状态"),
            health("报修工单", repairOrderMapper.findAll().size(), "工单流转状态"),
            health("公告活动", noticeMapper.findAll().size(), "公告发布和活动通知"),
            health("系统用户", sysUserMapper.findAll().size(), "账号角色和权限")
        );
    }

    private Map<String, Object> stat(String label, String value, String trend, String detail) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("label", label);
        item.put("value", value);
        item.put("trend", trend);
        item.put("detail", detail);
        return item;
    }

    private Map<String, Object> health(String module, long count, String description) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("module", module);
        item.put("count", count);
        item.put("description", description);
        return item;
    }

    private Object getMapValue(Map<String, Object> item, String upperKey, String lowerKey) {
        return item.containsKey(upperKey) ? item.get(upperKey) : item.get(lowerKey);
    }
}
