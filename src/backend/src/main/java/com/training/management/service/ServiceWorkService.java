package com.training.management.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.training.management.common.exception.BusinessException;
import com.training.management.domain.entity.Complaint;
import com.training.management.domain.entity.Notice;
import com.training.management.domain.entity.RepairOrder;
import com.training.management.mapper.ComplaintMapper;
import com.training.management.mapper.NoticeMapper;
import com.training.management.mapper.RepairOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ServiceWorkService {

    private final RepairOrderMapper repairOrderMapper;
    private final ComplaintMapper complaintMapper;
    private final NoticeMapper noticeMapper;

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
