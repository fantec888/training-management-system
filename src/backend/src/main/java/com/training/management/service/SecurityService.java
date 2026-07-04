package com.training.management.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.AccessControl;
import com.training.management.domain.entity.ParkingVehicle;
import com.training.management.domain.entity.PatrolTask;
import com.training.management.mapper.AccessControlMapper;
import com.training.management.mapper.ParkingVehicleMapper;
import com.training.management.mapper.PatrolTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final ParkingVehicleMapper parkingVehicleMapper;
    private final AccessControlMapper accessControlMapper;
    private final PatrolTaskMapper patrolTaskMapper;

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
