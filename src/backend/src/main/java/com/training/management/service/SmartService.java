package com.training.management.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.CommunityActivity;
import com.training.management.domain.entity.ExpressPackage;
import com.training.management.mapper.CommunityActivityMapper;
import com.training.management.mapper.ExpressPackageMapper;
import com.training.management.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SmartService {

    private final NoticeMapper noticeMapper;
    private final CommunityActivityMapper communityActivityMapper;
    private final ExpressPackageMapper expressPackageMapper;

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
        fillActivityDefaults(activity);
        communityActivityMapper.insert(activity);
        return activity;
    }

    public CommunityActivity updateActivity(Long id, CommunityActivity activity) {
        activity.setId(id);
        fillActivityDefaults(activity);
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

    private void fillActivityDefaults(CommunityActivity activity) {
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
