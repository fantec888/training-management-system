package com.training.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.training.management.common.PageResult;
import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.Building;
import com.training.management.domain.entity.Community;
import com.training.management.domain.entity.Resident;
import com.training.management.domain.entity.Room;
import com.training.management.mapper.BuildingMapper;
import com.training.management.mapper.CommunityMapper;
import com.training.management.mapper.ResidentMapper;
import com.training.management.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PropertyAssetService {

    private final ResidentMapper residentMapper;
    private final CommunityMapper communityMapper;
    private final BuildingMapper buildingMapper;
    private final RoomMapper roomMapper;

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

    public List<Resident> listResidents() {
        return residentMapper.findAll();
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
