package com.training.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.training.management.common.RequestContext;
import com.training.management.common.PageResult;
import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.AccessControl;
import com.training.management.domain.entity.Bill;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Community;
import com.training.management.domain.entity.CommunityActivity;
import com.training.management.domain.entity.Complaint;
import com.training.management.domain.entity.ExpressPackage;
import com.training.management.domain.entity.FeeItem;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.PatrolTask;
import com.training.management.domain.entity.ParkingVehicle;
import com.training.management.domain.entity.PaymentRecord;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.Room;
import com.training.management.domain.entity.SysUser;
import com.training.management.mapper.BillMapper;
import com.training.management.mapper.BuildingMapper;
import com.training.management.mapper.AccessControlMapper;
import com.training.management.mapper.CommunityMapper;
import com.training.management.mapper.CommunityActivityMapper;
import com.training.management.mapper.ComplaintMapper;
import com.training.management.mapper.ExpressPackageMapper;
import com.training.management.mapper.FeeItemMapper;
import com.training.management.mapper.NoticeMapper;
import com.training.management.mapper.PatrolTaskMapper;
import com.training.management.mapper.ParkingVehicleMapper;
import com.training.management.mapper.PaymentRecordMapper;
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
    private final CommunityMapper communityMapper;
    private final BuildingMapper buildingMapper;
    private final RoomMapper roomMapper;
    private final RepairOrderMapper repairOrderMapper;
    private final ComplaintMapper complaintMapper;
    private final BillMapper billMapper;
    private final FeeItemMapper feeItemMapper;
    private final PaymentRecordMapper paymentRecordMapper;
    private final ParkingVehicleMapper parkingVehicleMapper;
    private final NoticeMapper noticeMapper;
    private final CommunityActivityMapper communityActivityMapper;
    private final ExpressPackageMapper expressPackageMapper;
    private final AccessControlMapper accessControlMapper;
    private final PatrolTaskMapper patrolTaskMapper;
    private final SysUserMapper sysUserMapper;
    private final AuthService authService;

    public List<Resident> listResidents() {
        return residentMapper.findAll();
    }

    public List<Community> listCommunities() {
        return communityMapper.findAll();
    }

    public Community createCommunity(Community community) {
        requireText(community.getName(), "小区名称不能为空");
        requireText(community.getAddress(), "小区地址不能为空");
        fillCommunityDefaults(community);
        communityMapper.insert(community);
        return community;
    }

    public Community updateCommunity(Long id, Community community) {
        requireText(community.getName(), "小区名称不能为空");
        requireText(community.getAddress(), "小区地址不能为空");
        community.setId(id);
        fillCommunityDefaults(community);
        communityMapper.update(community);
        return community;
    }

    public void deleteCommunity(Long id) {
        communityMapper.deleteById(id);
    }

    public PageResult<Resident> pageResidents(int pageNum, int pageSize) {
        PageHelper.startPage(Math.max(pageNum, 1), Math.max(pageSize, 1));
        List<Resident> rows = residentMapper.findAll();
        PageInfo<Resident> page = new PageInfo<>(rows);
        return new PageResult<>(
            page.getList(),
            page.getTotal(),
            page.getPageNum(),
            page.getPageSize(),
            page.getPages()
        );
    }

    public Resident createResident(Resident resident) {
        requireText(resident.getName(), "住户姓名不能为空");
        requireText(resident.getPhone(), "住户电话不能为空");
        requireText(resident.getBuilding(), "楼栋不能为空");
        requireText(resident.getRoomNo(), "房号不能为空");
        fillResidentDefaults(resident);
        residentMapper.insert(resident);
        return resident;
    }

    public Resident updateResident(Long id, Resident resident) {
        requireText(resident.getName(), "住户姓名不能为空");
        requireText(resident.getPhone(), "住户电话不能为空");
        requireText(resident.getBuilding(), "楼栋不能为空");
        requireText(resident.getRoomNo(), "房号不能为空");
        resident.setId(id);
        fillResidentDefaults(resident);
        residentMapper.update(resident);
        return resident;
    }

    public void updateResidentStatus(Long id, String status) {
        requireText(status, "住户状态不能为空");
        residentMapper.updateStatus(id, status);
    }

    public void deleteResident(Long id) {
        residentMapper.deleteById(id);
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
        data.put("communities", communityMapper.findAll());
        data.put("buildings", buildingMapper.findAll());
        data.put("rooms", roomMapper.findAll());
        return data;
    }

    public Building createBuilding(Building building) {
        requireText(building.getName(), "楼栋名称不能为空");
        fillBuildingDefaults(building);
        buildingMapper.insert(building);
        return building;
    }

    public Building updateBuilding(Long id, Building building) {
        requireText(building.getName(), "楼栋名称不能为空");
        building.setId(id);
        fillBuildingDefaults(building);
        buildingMapper.update(building);
        return building;
    }

    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }

    public Room createRoom(Room room) {
        requireText(room.getBuildingName(), "楼栋不能为空");
        requireText(room.getUnitNo(), "单元不能为空");
        requireText(room.getRoomNo(), "房号不能为空");
        fillRoomDefaults(room);
        roomMapper.insert(room);
        return room;
    }

    public Room updateRoom(Long id, Room room) {
        requireText(room.getBuildingName(), "楼栋不能为空");
        requireText(room.getUnitNo(), "单元不能为空");
        requireText(room.getRoomNo(), "房号不能为空");
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

    public void deleteRoom(Long id) {
        roomMapper.deleteById(id);
    }

    public List<RepairOrder> listRepairs() {
        return repairOrderMapper.findAll();
    }

    public RepairOrder createRepair(RepairOrder repairOrder) {
        requireText(repairOrder.getTitle(), "报修问题不能为空");
        requireText(repairOrder.getArea(), "报修位置不能为空");
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

    public void deleteRepair(Long id) {
        repairOrderMapper.deleteById(id);
    }

    public List<Complaint> listComplaints() {
        return complaintMapper.findAll();
    }

    public Complaint createComplaint(Complaint complaint) {
        requireText(complaint.getResidentName(), "投诉人不能为空");
        requireText(complaint.getPhone(), "联系电话不能为空");
        requireText(complaint.getTitle(), "投诉标题不能为空");
        requireText(complaint.getContent(), "投诉内容不能为空");
        if (!StringUtils.hasText(complaint.getCode())) {
            complaint.setCode("TS" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
        }
        if (!StringUtils.hasText(complaint.getCategory())) {
            complaint.setCategory("服务建议");
        }
        if (!StringUtils.hasText(complaint.getStatus())) {
            complaint.setStatus("待处理");
        }
        if (complaint.getSubmittedAt() == null) {
            complaint.setSubmittedAt(LocalDateTime.now());
        }
        complaintMapper.insert(complaint);
        return complaint;
    }

    public void replyComplaint(Long id, Complaint complaint) {
        requireText(complaint.getReply(), "投诉回复内容不能为空");
        complaint.setId(id);
        if (!StringUtils.hasText(complaint.getHandler())) {
            complaint.setHandler("客服主管");
        }
        if (!StringUtils.hasText(complaint.getStatus())) {
            complaint.setStatus("已回复");
        }
        complaintMapper.updateReply(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintMapper.deleteById(id);
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
        requireText(notice.getTitle(), "公告标题不能为空");
        requireText(notice.getAudience(), "公告范围不能为空");
        requireText(notice.getPublisher(), "发布部门不能为空");
        if (notice.getPublishTime() == null) {
            notice.setPublishTime(LocalDateTime.now());
        }
        if (!StringUtils.hasText(notice.getStatus())) {
            notice.setStatus("已发布");
        }
        noticeMapper.insert(notice);
        return notice;
    }

    public void updateNoticeStatus(Long id, String status) {
        requireText(status, "公告状态不能为空");
        noticeMapper.updateStatus(id, status);
    }

    public void deleteNotice(Long id) {
        noticeMapper.deleteById(id);
    }

    public Map<String, Object> getSmartServices() {
        Map<String, Object> data = new LinkedHashMap<>();
        List<CommunityActivity> activities = communityActivityMapper.findAll();
        List<ExpressPackage> packages = expressPackageMapper.findAll();
        data.put("summary", List.of(
            summary("公告通知", String.valueOf(noticeMapper.countAll())),
            summary("社区活动", String.valueOf(activities.size())),
            summary("待领取快递", String.valueOf(packages.stream().filter(item -> !"已领取".equals(item.getStatus())).count()))
        ));
        data.put("notices", noticeMapper.findAll());
        data.put("activities", activities);
        data.put("packages", packages);
        return data;
    }

    public CommunityActivity createActivity(CommunityActivity activity) {
        requireText(activity.getTitle(), "活动标题不能为空");
        requireText(activity.getLocation(), "活动地点不能为空");
        requireText(activity.getOrganizer(), "组织部门不能为空");
        if (!StringUtils.hasText(activity.getActivityType())) {
            activity.setActivityType("社区活动");
        }
        if (activity.getStartTime() == null) {
            activity.setStartTime(LocalDateTime.now().plusDays(3));
        }
        if (activity.getSignups() == null) {
            activity.setSignups(0);
        }
        if (!StringUtils.hasText(activity.getStatus())) {
            activity.setStatus("报名中");
        }
        communityActivityMapper.insert(activity);
        return activity;
    }

    public CommunityActivity updateActivity(Long id, CommunityActivity activity) {
        activity.setId(id);
        createActivityDefaults(activity);
        communityActivityMapper.update(activity);
        return activity;
    }

    public void updateActivityStatus(Long id, String status) {
        requireText(status, "活动状态不能为空");
        communityActivityMapper.updateStatus(id, status);
    }

    public void deleteActivity(Long id) {
        communityActivityMapper.deleteById(id);
    }

    public ExpressPackage createPackage(ExpressPackage expressPackage) {
        requireText(expressPackage.getTrackingNo(), "快递单号不能为空");
        requireText(expressPackage.getRecipientName(), "收件人不能为空");
        requireText(expressPackage.getPhone(), "联系电话不能为空");
        if (!StringUtils.hasText(expressPackage.getPickupCode())) {
            expressPackage.setPickupCode("P" + DateTimeFormatter.ofPattern("HHmm").format(LocalDateTime.now()));
        }
        if (!StringUtils.hasText(expressPackage.getStatus())) {
            expressPackage.setStatus("待领取");
        }
        if (expressPackage.getArrivedAt() == null) {
            expressPackage.setArrivedAt(LocalDateTime.now());
        }
        expressPackageMapper.insert(expressPackage);
        return expressPackage;
    }

    public ExpressPackage updatePackage(Long id, ExpressPackage expressPackage) {
        expressPackage.setId(id);
        if (expressPackage.getArrivedAt() == null) {
            expressPackage.setArrivedAt(LocalDateTime.now());
        }
        expressPackageMapper.update(expressPackage);
        return expressPackage;
    }

    public void updatePackageStatus(Long id, String status) {
        requireText(status, "快递状态不能为空");
        ExpressPackage expressPackage = new ExpressPackage();
        expressPackage.setId(id);
        expressPackage.setStatus(status);
        expressPackage.setPickedAt("已领取".equals(status) ? LocalDateTime.now() : null);
        expressPackageMapper.updateStatus(expressPackage);
    }

    public void deletePackage(Long id) {
        expressPackageMapper.deleteById(id);
    }

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

    public Map<String, Object> getSecurity() {
        List<AccessControl> accessControls = accessControlMapper.findAll();
        List<PatrolTask> patrols = patrolTaskMapper.findAll();
        List<ParkingVehicle> vehicles = parkingVehicleMapper.findAll();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("summary", List.of(
            summary("门禁设备", String.valueOf(accessControls.size())),
            summary("在线设备", String.valueOf(accessControls.stream().filter(item -> "在线".equals(item.getStatus())).count())),
            summary("车辆档案", String.valueOf(vehicles.size())),
            summary("待巡检", String.valueOf(patrols.stream().filter(item -> !"已完成".equals(item.getStatus())).count()))
        ));
        data.put("accessControls", accessControls);
        data.put("vehicles", vehicles);
        data.put("patrols", patrols);
        return data;
    }

    public AccessControl createAccessControl(AccessControl accessControl) {
        requireText(accessControl.getDeviceName(), "门禁设备名称不能为空");
        requireText(accessControl.getGateName(), "门禁位置不能为空");
        requireText(accessControl.getManager(), "负责人不能为空");
        if (!StringUtils.hasText(accessControl.getDeviceType())) {
            accessControl.setDeviceType("人脸识别");
        }
        if (!StringUtils.hasText(accessControl.getStatus())) {
            accessControl.setStatus("在线");
        }
        if (accessControl.getLastCheckAt() == null) {
            accessControl.setLastCheckAt(LocalDateTime.now());
        }
        accessControlMapper.insert(accessControl);
        return accessControl;
    }

    public AccessControl updateAccessControl(Long id, AccessControl accessControl) {
        accessControl.setId(id);
        if (accessControl.getLastCheckAt() == null) {
            accessControl.setLastCheckAt(LocalDateTime.now());
        }
        accessControlMapper.update(accessControl);
        return accessControl;
    }

    public void updateAccessStatus(Long id, String status) {
        requireText(status, "设备状态不能为空");
        accessControlMapper.updateStatus(id, status);
    }

    public void deleteAccessControl(Long id) {
        accessControlMapper.deleteById(id);
    }

    public PatrolTask createPatrol(PatrolTask patrolTask) {
        requireText(patrolTask.getRouteName(), "巡检路线不能为空");
        requireText(patrolTask.getArea(), "巡检区域不能为空");
        requireText(patrolTask.getAssignee(), "巡检人员不能为空");
        if (patrolTask.getPlanTime() == null) {
            patrolTask.setPlanTime(LocalDateTime.now().plusHours(2));
        }
        if (!StringUtils.hasText(patrolTask.getStatus())) {
            patrolTask.setStatus("待巡检");
        }
        patrolTaskMapper.insert(patrolTask);
        return patrolTask;
    }

    public PatrolTask updatePatrol(Long id, PatrolTask patrolTask) {
        patrolTask.setId(id);
        patrolTaskMapper.update(patrolTask);
        return patrolTask;
    }

    public void finishPatrol(Long id, PatrolTask patrolTask) {
        patrolTask.setId(id);
        if (!StringUtils.hasText(patrolTask.getStatus())) {
            patrolTask.setStatus("已完成");
        }
        if (patrolTask.getFinishedAt() == null && "已完成".equals(patrolTask.getStatus())) {
            patrolTask.setFinishedAt(LocalDateTime.now());
        }
        patrolTaskMapper.updateResult(patrolTask);
    }

    public void deletePatrol(Long id) {
        patrolTaskMapper.deleteById(id);
    }

    public List<SysUser> listSystemUsers() {
        return sysUserMapper.findAll();
    }

    public SysUser createSystemUser(SysUser user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new BusinessException(400, "账号不能为空");
        }
        requireText(user.getRealName(), "姓名不能为空");
        requireText(user.getRoleCode(), "角色不能为空");
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
        requireText(user.getRealName(), "姓名不能为空");
        requireText(user.getRoleCode(), "角色不能为空");
        user.setId(id);
        fillUserDefaults(user);
        sysUserMapper.update(user);
        return user;
    }

    public void updateSystemUserStatus(Long id, Boolean enabled) {
        if (Boolean.FALSE.equals(enabled)) {
            preventCurrentUserRemoval(id, "不能冻结当前登录账号");
        }
        sysUserMapper.updateEnabled(id, enabled);
    }

    public void deleteSystemUser(Long id) {
        preventCurrentUserRemoval(id, "不能删除当前登录账号");
        sysUserMapper.deleteById(id);
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

    private void fillCommunityDefaults(Community community) {
        if (community.getTotalBuildings() == null) {
            community.setTotalBuildings(0);
        }
        if (community.getTotalRooms() == null) {
            community.setTotalRooms(0);
        }
        if (!StringUtils.hasText(community.getPropertyCompany())) {
            community.setPropertyCompany("云栖物业服务有限公司");
        }
        if (!StringUtils.hasText(community.getManager())) {
            community.setManager("物业经理");
        }
        if (!StringUtils.hasText(community.getStatus())) {
            community.setStatus("正常");
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

    private void createActivityDefaults(CommunityActivity activity) {
        requireText(activity.getTitle(), "活动标题不能为空");
        requireText(activity.getLocation(), "活动地点不能为空");
        requireText(activity.getOrganizer(), "组织部门不能为空");
        if (!StringUtils.hasText(activity.getActivityType())) {
            activity.setActivityType("社区活动");
        }
        if (activity.getStartTime() == null) {
            activity.setStartTime(LocalDateTime.now().plusDays(3));
        }
        if (activity.getSignups() == null) {
            activity.setSignups(0);
        }
        if (!StringUtils.hasText(activity.getStatus())) {
            activity.setStatus("报名中");
        }
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

    private void preventCurrentUserRemoval(Long id, String message) {
        SysUser currentUser = RequestContext.getCurrentUser();
        if (currentUser != null && id != null && id.equals(currentUser.getId())) {
            throw new BusinessException(400, message);
        }
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new BusinessException(400, message);
        }
    }

    private Map<String, Object> summary(String label, String value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("label", label);
        item.put("value", value);
        return item;
    }
}
