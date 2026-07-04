INSERT INTO sys_user (username, password_hash, real_name, role_code, department, permission_scope, phone, enabled, last_login_at)
VALUES
('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '系统管理员', 'SUPER_ADMIN', '平台运营中心', '全量权限', '13800000001', 1, '2026-07-01 08:36:00'),
('service_manager', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '客服主管', 'SERVICE_MANAGER', '客户服务部', '工单、公告、住户', '13800000002', 1, '2026-07-01 09:02:00'),
('finance_admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '财务专员', 'FINANCE_ADMIN', '财务部', '收费、报表', '13800000003', 1, '2026-06-30 18:11:00'),
('engineer_lead', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '工程组长', 'ENGINEER_LEAD', '工程维修部', '工单、设备', '13800000004', 0, '2026-06-30 20:30:00');

INSERT INTO sys_role (id, role_code, role_name, description, enabled, sort_order)
VALUES
(1, 'SUPER_ADMIN', '系统管理员', '拥有后台全部菜单、接口和按钮权限', 1, 1),
(2, 'SERVICE_MANAGER', '物业客服主管', '负责住户、报修、公告和停车等日常服务', 1, 2),
(3, 'FINANCE_ADMIN', '财务专员', '负责收费账单、缴费确认和财务统计', 1, 3),
(4, 'ENGINEER_LEAD', '工程维修组长', '负责楼栋房屋、设备巡检和维修工单处理', 1, 4);

INSERT INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, route_path, component_key, sort_order, visible)
VALUES
(1, 'menu:dashboard', '首页仪表盘', 'MENU', NULL, '/', 'dashboard', 1, 1),
(2, 'menu:residents', '住户管理', 'MENU', NULL, '/residents', 'residents', 2, 1),
(3, 'menu:properties', '楼栋房屋', 'MENU', NULL, '/properties', 'properties', 3, 1),
(4, 'menu:repairs', '报修工单', 'MENU', NULL, '/repairs', 'repairs', 4, 1),
(5, 'menu:billing', '收费管理', 'MENU', NULL, '/billing', 'billing', 5, 1),
(6, 'menu:parking', '停车管理', 'MENU', NULL, '/parking', 'parking', 6, 1),
(7, 'menu:notices', '公告活动', 'MENU', NULL, '/notices', 'notices', 7, 1),
(8, 'menu:system-users', '系统用户', 'MENU', NULL, '/system-users', 'system-users', 8, 1),
(9, 'button:resident:create', '新增住户', 'BUTTON', 2, NULL, 'residents', 21, 0),
(10, 'button:resident:update', '编辑住户', 'BUTTON', 2, NULL, 'residents', 22, 0),
(11, 'button:resident:delete', '删除住户', 'BUTTON', 2, NULL, 'residents', 23, 0),
(12, 'button:repair:create', '创建工单', 'BUTTON', 4, NULL, 'repairs', 41, 0),
(13, 'button:repair:update', '更新工单进度', 'BUTTON', 4, NULL, 'repairs', 42, 0),
(14, 'button:repair:delete', '删除工单', 'BUTTON', 4, NULL, 'repairs', 43, 0),
(15, 'button:bill:create', '创建账单', 'BUTTON', 5, NULL, 'billing', 51, 0),
(16, 'button:bill:update', '更新账单状态', 'BUTTON', 5, NULL, 'billing', 52, 0),
(17, 'button:bill:delete', '删除账单', 'BUTTON', 5, NULL, 'billing', 53, 0),
(18, 'button:notice:create', '发布公告', 'BUTTON', 7, NULL, 'notices', 71, 0),
(19, 'button:notice:delete', '删除公告', 'BUTTON', 7, NULL, 'notices', 72, 0),
(20, 'button:user:create', '新增系统用户', 'BUTTON', 8, NULL, 'system-users', 81, 0),
(21, 'button:user:update', '编辑系统用户', 'BUTTON', 8, NULL, 'system-users', 82, 0),
(22, 'button:user:delete', '删除系统用户', 'BUTTON', 8, NULL, 'system-users', 83, 0),
(23, 'button:role:manage', '角色管理', 'BUTTON', 8, NULL, 'system-users', 84, 0),
(24, 'button:permission:manage', '菜单权限管理', 'BUTTON', 8, NULL, 'system-users', 85, 0),
(25, 'button:user-role:assign', '用户角色分配', 'BUTTON', 8, NULL, 'system-users', 86, 0),
(26, 'button:role-permission:assign', '角色权限分配', 'BUTTON', 8, NULL, 'system-users', 87, 0);

INSERT INTO sys_user_role (user_id, role_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission
WHERE permission_code IN (
    'menu:dashboard', 'menu:residents', 'menu:properties', 'menu:repairs', 'menu:parking', 'menu:notices',
    'button:resident:create', 'button:resident:update', 'button:resident:delete',
    'button:repair:create', 'button:repair:update',
    'button:notice:create', 'button:notice:delete'
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission
WHERE permission_code IN (
    'menu:dashboard', 'menu:residents', 'menu:billing', 'menu:parking',
    'button:bill:create', 'button:bill:update', 'button:bill:delete'
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 4, id FROM sys_permission
WHERE permission_code IN (
    'menu:dashboard', 'menu:properties', 'menu:repairs', 'menu:parking',
    'button:repair:update', 'button:repair:delete'
);

INSERT INTO resident (name, identity_type, phone, building, room_no, vehicles, verified_status, tag, status)
VALUES
('林晓雯', '业主', '138****1024', 'A1', '1203', 2, '已认证', '高频报修', '正常'),
('赵启航', '租户', '139****7611', 'A2', '0901', 1, '已认证', '按时缴费', '正常'),
('孙雅琪', '业主', '136****4817', 'B3', '1602', 0, '待审核', '新入住', '审核中'),
('周景川', '业主', '137****9372', 'C1', '2106', 1, '已认证', 'VIP 住户', '正常'),
('郭子涵', '租户', '135****2148', 'B1', '0805', 1, '已认证', '临停较多', '正常'),
('何曼', '业主', '133****7712', 'A1', '1808', 0, '已认证', '空巢关怀', '正常');

INSERT INTO building (name, floors, units, occupancy_rate, vacant_count, manager, status)
VALUES
('A1', 32, 2, 96.00, 8, '李工', '正常'),
('A2', 28, 2, 91.00, 15, '陈工', '正常'),
('B1', 26, 3, 89.00, 22, '王工', '正常'),
('B3', 33, 2, 94.00, 11, '徐工', '正常'),
('C1', 24, 2, 87.00, 26, '何工', '巡检中');

INSERT INTO room (building_name, unit_no, room_no, floor_no, area, resident_name, status)
VALUES
('A1', '1单元', '1203', 12, 108.50, '林晓雯', '已入住'),
('A1', '1单元', '1808', 18, 96.00, '何曼', '已入住'),
('A1', '2单元', '0602', 6, 88.00, NULL, '空置'),
('A2', '1单元', '0901', 9, 92.00, '赵启航', '已入住'),
('A2', '2单元', '1406', 14, 118.00, NULL, '装修中'),
('B1', '1单元', '0805', 8, 86.00, '郭子涵', '已入住'),
('B3', '2单元', '1602', 16, 101.00, '孙雅琪', '已入住'),
('C1', '1单元', '2106', 21, 126.00, '周景川', '已入住'),
('C1', '2单元', '1201', 12, 96.00, NULL, '空置');

INSERT INTO repair_order (code, title, area, priority, assignee, status, duration_hours, created_at)
VALUES
('WX20260701001', '地下车库排风异常', '车库 B 区', '高', '工程班组 2', '处理中', 2.00, '2026-07-01 08:10:00'),
('WX20260701004', 'A2 栋门禁失灵', 'A2 单元大堂', '中', '安防组', '待派单', 0.60, '2026-07-01 09:30:00'),
('WX20260630019', 'B1-1002 厨房漏水', 'B1-1002', '高', '维修员 周凯', '待回访', 5.00, '2026-06-30 15:15:00'),
('WX20260629011', '儿童区照明闪烁', '中央花园', '低', '物业电工', '已完成', 1.00, '2026-06-29 18:30:00'),
('WX20260628008', '楼道墙面破损', 'C1-12F', '低', '工程班组 1', '已完成', 3.50, '2026-06-28 10:00:00');

INSERT INTO bill (resident_name, house, fee_type, bill_period, amount, status, due_date)
VALUES
('林晓雯', 'A1-1203', '物业费', '2026-07', 428.00, '已缴费', '2026-07-10'),
('赵启航', 'A2-0901', '水电费', '2026-07', 312.50, '待缴费', '2026-07-12'),
('孙雅琪', 'B3-1602', '停车月租', '2026-07', 260.00, '待缴费', '2026-07-12'),
('周景川', 'C1-2106', '物业费', '2026-07', 538.00, '已缴费', '2026-07-10'),
('郭子涵', 'B1-0805', '水电费', '2026-07', 286.80, '逾期', '2026-06-28');

INSERT INTO parking_vehicle (plate, owner, vehicle_type, location, status, monthly_fee)
VALUES
('粤B·7T823', '林晓雯', '固定车', 'B2-118', '正常', 260.00),
('粤B·9K515', '赵启航', '固定车', 'A1-072', '续费中', 260.00),
('粤B·3Q221', '访客车辆', '临停车', '访客区-12', '在场', 0.00),
('粤B·6M907', '周景川', '固定车', 'C1-205', '正常', 260.00);

INSERT INTO notice (title, category, audience, publisher, publish_time, status)
VALUES
('7 月供水管道检修通知', '通知', '全体住户', '客服中心', '2026-07-01 09:00:00', '已发布'),
('夏日清凉市集报名开启', '活动', '青年家庭', '社区运营', '2026-06-30 16:20:00', '报名中'),
('电动车规范停放提醒', '安全', '全体住户', '秩序维护部', '2026-06-29 18:40:00', '已发布'),
('地下车库临停收费调整', '通知', '车主用户', '财务部', '2026-06-29 12:00:00', '已发布');

INSERT INTO monthly_revenue (month_label, amount)
VALUES
('1月', 82.00),
('2月', 88.00),
('3月', 95.00),
('4月', 103.00),
('5月', 110.00),
('6月', 124.00);

INSERT INTO todo_item (title, deadline, level, completed)
VALUES
('A2 栋电梯年检', '今天 14:00', '高', 0),
('6 月账单欠费回访', '今天 16:30', '中', 0),
('周末亲子活动审批', '明天 10:00', '低', 0),
('地下车库摄像头巡检', '明天 15:00', '中', 0);
