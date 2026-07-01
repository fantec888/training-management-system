package com.training.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.ParkingVehicle;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.Room;
import com.training.management.domain.entity.SysUser;
import com.training.management.mapper.BillMapper;
import com.training.management.mapper.BuildingMapper;
import com.training.management.mapper.NoticeMapper;
import com.training.management.mapper.ParkingVehicleMapper;
import com.training.management.mapper.RepairOrderMapper;
import com.training.management.mapper.ResidentMapper;
import com.training.management.mapper.RoomMapper;
import com.training.management.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final ResidentMapper residentMapper;
    private final BuildingMapper buildingMapper;
    private final RoomMapper roomMapper;
    private final RepairOrderMapper repairOrderMapper;
    private final BillMapper billMapper;
    private final ParkingVehicleMapper parkingVehicleMapper;
    private final NoticeMapper noticeMapper;
    private final SysUserMapper sysUserMapper;
    private final AuthService authService;

    public List<Resident> listResidents() {
        return residentMapper.findAll();
    }

    public Resident createResident(Resident resident) {
        fillResidentDefaults(resident);
        residentMapper.insert(resident);
        return resident;
    }

    public Resident updateResident(Long id, Resident resident) {
        resident.setId(id);
        fillResidentDefaults(resident);
        residentMapper.update(resident);
        return resident;
    }

    public void updateResidentStatus(Long id, String status) {
        if (!StringUtils.hasText(status)) {
            throw new BusinessException(400, "住户状态不能为空");
        }
        residentMapper.updateStatus(id, status);
    }

    public Map<String, Object> getProperties() {
        long totalRooms = roomMapper.countAll();
        long occupiedRooms = roomMapper.countOccupied();
        BigDecimal occupancyRate = totalRooms == 0
            ? BigDecimal.ZERO
            : BigDecimal.valueOf(occupiedRooms)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRooms), 1, RoundingMode.HALF_UP);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("summary", List.of(
            summary("管理楼栋", buildingMapper.countAll() + " 栋"),
            summary("房屋总数", totalRooms + " 套"),
            summary("入住率", occupancyRate + "%"),
            summary("空置房屋", roomMapper.countVacant() + " 套")
        ));
        data.put("buildings", buildingMapper.findAll());
        data.put("rooms", roomMapper.findAll());
        return data;
    }

    public Building createBuilding(Building building) {
        fillBuildingDefaults(building);
        buildingMapper.insert(building);
        return building;
    }

    public Building updateBuilding(Long id, Building building) {
        building.setId(id);
        fillBuildingDefaults(building);
        buildingMapper.update(building);
        return building;
    }

    public Room createRoom(Room room) {
        fillRoomDefaults(room);
        roomMapper.insert(room);
        return room;
    }

    public Room updateRoom(Long id, Room room) {
        room.setId(id);
        fillRoomDefaults(room);
        roomMapper.update(room);
        return room;
    }

    public void bindRoomResident(Long id, String residentName, String status) {
        String targetStatus = StringUtils.hasText(status)
            ? status
            : (StringUtils.hasText(residentName) ? "已入住" : "空置");
        roomMapper.bindResident(id, residentName, targetStatus);
    }

    public List<RepairOrder> listRepairs() {
        return repairOrderMapper.findAll();
    }

    public RepairOrder createRepair(RepairOrder repairOrder) {
        if (!StringUtils.hasText(repairOrder.getCode())) {
            repairOrder.setCode("WX" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        }
        if (!StringUtils.hasText(repairOrder.getPriority())) {
            repairOrder.setPriority("中");
        }
        if (!StringUtils.hasText(repairOrder.getStatus())) {
            repairOrder.setStatus("待处理");
        }
        if (!StringUtils.hasText(repairOrder.getAssignee())) {
            repairOrder.setAssignee("待派单");
        }
        if (repairOrder.getDurationHours() == null) {
            repairOrder.setDurationHours(BigDecimal.ZERO);
        }
        repairOrderMapper.insert(repairOrder);
        return repairOrder;
    }

    public void updateRepairProgress(Long id, RepairOrder repairOrder) {
        String status = StringUtils.hasText(repairOrder.getStatus()) ? repairOrder.getStatus() : "待处理";
        String assignee = StringUtils.hasText(repairOrder.getAssignee()) ? repairOrder.getAssignee() : "待派单";
        BigDecimal durationHours = repairOrder.getDurationHours() == null
            ? BigDecimal.ZERO
            : repairOrder.getDurationHours();
        repairOrderMapper.updateProgress(id, status, assignee, durationHours);
    }

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
        return data;
    }

    public Bill createBill(Bill bill) {
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
        if (!StringUtils.hasText(status)) {
            throw new BusinessException(400, "账单状态不能为空");
        }
        billMapper.updateStatus(id, status);
    }

    public Map<String, Object> getParking() {
        List<ParkingVehicle> vehicles = parkingVehicleMapper.findAll();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stats", List.of(
            summary("总车位", "1340"),
            summary("已出租", String.valueOf(parkingVehicleMapper.countFixed())),
            summary("临停车位", String.valueOf(parkingVehicleMapper.countTemporary())),
            summary("空闲车位", "124")
        ));
        data.put("vehicles", vehicles);
        return data;
    }

    public Map<String, Object> getNotices() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("summary", List.of(
            summary("本月公告", String.valueOf(noticeMapper.countAll())),
            summary("活动策划", String.valueOf(noticeMapper.countActivities())),
            summary("短信触达", "8,436")
        ));
        data.put("list", noticeMapper.findAll());
        return data;
    }

    public Notice createNotice(Notice notice) {
        if (notice.getPublishTime() == null) {
            notice.setPublishTime(LocalDateTime.now());
        }
        if (!StringUtils.hasText(notice.getStatus())) {
            notice.setStatus("已发布");
        }
        noticeMapper.insert(notice);
        return notice;
    }

    public List<SysUser> listSystemUsers() {
        return sysUserMapper.findAll();
    }

    public SysUser createSystemUser(SysUser user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new BusinessException(400, "账号不能为空");
        }
        if (sysUserMapper.findByUsername(user.getUsername()) != null) {
            throw new BusinessException(400, "账号已存在");
        }
        fillUserDefaults(user);
        if (!StringUtils.hasText(user.getPasswordHash())) {
            user.setPasswordHash(authService.sha256("123456"));
        }
        sysUserMapper.insert(user);
        return user;
    }

    public SysUser updateSystemUser(Long id, SysUser user) {
        user.setId(id);
        fillUserDefaults(user);
        sysUserMapper.update(user);
        return user;
    }

    public void updateSystemUserStatus(Long id, Boolean enabled) {
        sysUserMapper.updateEnabled(id, enabled);
    }

    private void fillResidentDefaults(Resident resident) {
        if (resident.getVehicles() == null) {
            resident.setVehicles(0);
        }
        if (!StringUtils.hasText(resident.getVerifiedStatus())) {
            resident.setVerifiedStatus("待审核");
        }
        if (!StringUtils.hasText(resident.getStatus())) {
            resident.setStatus("正常");
        }
    }

    private void fillBuildingDefaults(Building building) {
        if (building.getFloors() == null) {
            building.setFloors(1);
        }
        if (building.getUnits() == null) {
            building.setUnits(1);
        }
        if (building.getOccupancyRate() == null) {
            building.setOccupancyRate(BigDecimal.ZERO);
        }
        if (building.getVacantCount() == null) {
            building.setVacantCount(0);
        }
        if (!StringUtils.hasText(building.getStatus())) {
            building.setStatus("正常");
        }
    }

    private void fillRoomDefaults(Room room) {
        if (room.getFloorNo() == null) {
            room.setFloorNo(1);
        }
        if (room.getArea() == null) {
            room.setArea(BigDecimal.ZERO);
        }
        if (!StringUtils.hasText(room.getStatus())) {
            room.setStatus(StringUtils.hasText(room.getResidentName()) ? "已入住" : "空置");
        }
    }

    private void fillUserDefaults(SysUser user) {
        if (!StringUtils.hasText(user.getRoleCode())) {
            user.setRoleCode("SERVICE_MANAGER");
        }
        if (!StringUtils.hasText(user.getPermissionScope())) {
            user.setPermissionScope("住户、工单、公告");
        }
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
    }

    private Map<String, Object> summary(String label, String value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("label", label);
        item.put("value", value);
        return item;
    }
}
