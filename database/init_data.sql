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

INSERT INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, route_path, component_key, sort_order, visible)
VALUES
(27, 'menu:communities', '小区管理', 'MENU', NULL, '/communities', 'communities', 9, 1),
(28, 'menu:complaints', '投诉建议', 'MENU', NULL, '/complaints', 'complaints', 10, 1),
(29, 'button:community:manage', '小区管理维护', 'BUTTON', 27, NULL, 'communities', 91, 0),
(30, 'button:complaint:handle', '投诉处理回复', 'BUTTON', 28, NULL, 'complaints', 101, 0),
(31, 'button:fee-item:manage', '费用项目管理', 'BUTTON', 5, NULL, 'billing', 54, 0),
(32, 'button:payment:create', '缴费登记', 'BUTTON', 5, NULL, 'billing', 55, 0);

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

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission
WHERE permission_code IN (
    'menu:communities', 'menu:complaints',
    'button:community:manage', 'button:complaint:handle'
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission
WHERE permission_code IN ('button:fee-item:manage', 'button:payment:create');

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 4, id FROM sys_permission
WHERE permission_code IN ('menu:communities', 'button:community:manage');

INSERT INTO community (name, address, developer, property_company, total_buildings, total_rooms, manager, status)
VALUES
('云栖花园', '东城区云栖路 88 号', '云城置业', '云栖物业服务有限公司', 5, 1260, '李经理', '正常'),
('滨河雅苑', '滨河新区星河大道 16 号', '星河地产', '星河物业', 3, 720, '陈经理', '正常'),
('未来城一期', '高新区未来路 99 号', '未来控股', '未来城物业中心', 4, 980, '王经理', '筹备中');

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

INSERT INTO complaint (code, resident_name, phone, category, title, content, handler, reply, status, submitted_at, replied_at)
VALUES
('TS20260701001', '林晓雪', '138****1024', '环境卫生', '地下车库异味较重', 'B2 车库靠近垃圾房区域异味明显，希望尽快处理。', '客服主管', '已安排保洁和工程联合排查通风系统。', '处理中', '2026-07-01 10:20:00', '2026-07-01 11:05:00'),
('TS20260701002', '赵启航', '139****7611', '噪音投诉', '夜间装修噪音', 'A2 楼 14 层夜间仍有装修声音，影响休息。', NULL, NULL, '待处理', '2026-07-01 21:30:00', NULL),
('TS20260630003', '周景川', '137****9372', '服务建议', '建议增加儿童区遮阳设施', '夏季儿童活动区较晒，建议增加遮阳棚。', '客服主管', '已纳入社区设施优化计划。', '已回复', '2026-06-30 16:45:00', '2026-07-01 09:00:00');

INSERT INTO bill (resident_name, house, fee_type, bill_period, amount, status, due_date)
VALUES
('林晓雯', 'A1-1203', '物业费', '2026-07', 428.00, '已缴费', '2026-07-10'),
('赵启航', 'A2-0901', '水电费', '2026-07', 312.50, '待缴费', '2026-07-12'),
('孙雅琪', 'B3-1602', '停车月租', '2026-07', 260.00, '待缴费', '2026-07-12'),
('周景川', 'C1-2106', '物业费', '2026-07', 538.00, '已缴费', '2026-07-10'),
('郭子涵', 'B1-0805', '水电费', '2026-07', 286.80, '逾期', '2026-06-28');

INSERT INTO fee_item (name, category, billing_cycle, unit_price, description, enabled)
VALUES
('物业管理费', '物业费', '月度', 3.20, '按房屋面积每月生成账单', 1),
('停车月租费', '停车费', '月度', 260.00, '固定车位月租费用', 1),
('生活垃圾处理费', '公共服务', '月度', 18.00, '按户收取的公共服务费用', 1),
('临时停车费', '停车费', '按次', 8.00, '访客车辆临停计费', 1);

INSERT INTO payment_record (bill_id, payer_name, house, amount, payment_method, operator, remark, paid_at)
VALUES
(1, '林晓雪', 'A1-1203', 428.00, '微信支付', '财务专员', '物业费线上缴费', '2026-07-03 09:18:00'),
(4, '周景川', 'C1-2106', 538.00, '银行卡', '财务专员', '柜台确认收款', '2026-07-02 15:42:00');

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

INSERT INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, route_path, component_key, sort_order, visible)
VALUES
(33, 'menu:smart-services', '智能服务', 'MENU', NULL, '/smart-services', 'smart-services', 11, 1),
(34, 'menu:statistics', '数据统计', 'MENU', NULL, '/statistics', 'statistics', 12, 1),
(35, 'menu:security', '安防管理', 'MENU', NULL, '/security', 'security', 13, 1),
(36, 'button:activity:manage', '社区活动管理', 'BUTTON', 33, NULL, 'smart-services', 111, 0),
(37, 'button:package:manage', '快递代收管理', 'BUTTON', 33, NULL, 'smart-services', 112, 0),
(38, 'button:access:manage', '门禁设备管理', 'BUTTON', 35, NULL, 'security', 131, 0),
(39, 'button:patrol:manage', '巡检任务管理', 'BUTTON', 35, NULL, 'security', 132, 0);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE id >= 33;

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission
WHERE permission_code IN (
    'menu:smart-services', 'menu:statistics', 'menu:security',
    'button:activity:manage', 'button:package:manage',
    'button:access:manage', 'button:patrol:manage'
);

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission WHERE permission_code IN ('menu:statistics');

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 4, id FROM sys_permission
WHERE permission_code IN ('menu:statistics', 'menu:security', 'button:access:manage', 'button:patrol:manage');

INSERT INTO community_activity (title, activity_type, location, organizer, start_time, signups, status)
VALUES
('夏日清凉市集', '社区活动', '中心广场', '社区运营部', '2026-07-06 18:30:00', 86, '报名中'),
('亲子手工课堂', '亲子活动', '党群活动室', '客服中心', '2026-07-08 15:00:00', 32, '报名中'),
('消防安全公开课', '安全培训', 'B区架空层', '秩序维护部', '2026-07-10 09:30:00', 64, '筹备中');

INSERT INTO express_package (tracking_no, recipient_name, phone, pickup_code, cabinet_no, status, arrived_at, picked_at)
VALUES
('SF1002458891', '林晓雪', '138****1024', 'A739', '1号柜-03', '待领取', '2026-07-04 08:30:00', NULL),
('YT8820911345', '赵启航', '139****7611', 'K216', '2号柜-12', '已领取', '2026-07-03 16:20:00', '2026-07-03 19:10:00'),
('JD7712098760', '周景川', '137****9372', 'C508', '前台货架', '待领取', '2026-07-04 09:15:00', NULL);

INSERT INTO access_control (device_name, gate_name, device_type, status, manager, last_check_at)
VALUES
('A1人脸门禁01', 'A1一单元大堂', '人脸识别', '在线', '秩序班组1', '2026-07-04 08:00:00'),
('B2车库道闸02', '地下车库B入口', '车辆道闸', '在线', '秩序班组2', '2026-07-04 08:20:00'),
('C1访客机01', 'C1大堂', '访客登记', '离线', '秩序班组1', '2026-07-03 22:10:00');

INSERT INTO patrol_task (route_name, area, assignee, plan_time, status, result, finished_at)
VALUES
('早班公共区域巡检', 'A区大堂/电梯/消防通道', '王安保', '2026-07-04 09:00:00', '已完成', '公共照明正常，A2大堂门禁需复查', '2026-07-04 09:40:00'),
('车库夜间巡检', 'B1-B2地下车库', '刘安保', '2026-07-04 22:00:00', '待巡检', NULL, NULL),
('消防设施巡检', 'C1楼栋消防箱', '陈工程', '2026-07-05 10:00:00', '待巡检', NULL, NULL);
