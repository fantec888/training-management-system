DROP TABLE IF EXISTS todo_item;
DROP TABLE IF EXISTS monthly_revenue;
DROP TABLE IF EXISTS patrol_task;
DROP TABLE IF EXISTS access_control;
DROP TABLE IF EXISTS express_package;
DROP TABLE IF EXISTS community_activity;
DROP TABLE IF EXISTS payment_record;
DROP TABLE IF EXISTS fee_item;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS parking_vehicle;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS complaint;
DROP TABLE IF EXISTS repair_order;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS resident;
DROP TABLE IF EXISTS community;
DROP TABLE IF EXISTS sys_role_permission;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_permission;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    department VARCHAR(80),
    permission_scope VARCHAR(255),
    phone VARCHAR(20),
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    last_login_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(80) NOT NULL,
    description VARCHAR(255),
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name VARCHAR(80) NOT NULL,
    permission_type VARCHAR(20) NOT NULL,
    parent_id BIGINT,
    route_path VARCHAR(120),
    component_key VARCHAR(80),
    sort_order INT NOT NULL DEFAULT 0,
    visible TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id)
);

CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (role_id, permission_id)
);

CREATE TABLE community (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    address VARCHAR(160) NOT NULL,
    developer VARCHAR(80),
    property_company VARCHAR(80) NOT NULL,
    total_buildings INT NOT NULL DEFAULT 0,
    total_rooms INT NOT NULL DEFAULT 0,
    manager VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT '正常',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE resident (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    identity_type VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    building VARCHAR(20) NOT NULL,
    room_no VARCHAR(20) NOT NULL,
    vehicles INT NOT NULL DEFAULT 0,
    verified_status VARCHAR(20) NOT NULL,
    tag VARCHAR(30),
    status VARCHAR(20) NOT NULL DEFAULT '正常',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    community_name VARCHAR(80) NOT NULL DEFAULT '云栖花园',
    name VARCHAR(20) NOT NULL,
    floors INT NOT NULL,
    units INT NOT NULL,
    occupancy_rate DECIMAL(5,2) NOT NULL,
    vacant_count INT NOT NULL,
    manager VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT '正常'
);

CREATE TABLE room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    building_name VARCHAR(20) NOT NULL,
    unit_no VARCHAR(20) NOT NULL,
    room_no VARCHAR(20) NOT NULL,
    floor_no INT NOT NULL,
    area DECIMAL(8,2) NOT NULL,
    resident_name VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT '空置',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (building_name, unit_no, room_no)
);

CREATE TABLE repair_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL UNIQUE,
    title VARCHAR(120) NOT NULL,
    area VARCHAR(120) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    assignee VARCHAR(80) NOT NULL,
    status VARCHAR(20) NOT NULL,
    duration_hours DECIMAL(6,2) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE complaint (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(30) NOT NULL UNIQUE,
    resident_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    title VARCHAR(120) NOT NULL,
    content VARCHAR(500) NOT NULL,
    handler VARCHAR(80),
    reply VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT '待处理',
    submitted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    replied_at DATETIME
);

CREATE TABLE bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resident_name VARCHAR(50) NOT NULL,
    house VARCHAR(30) NOT NULL,
    fee_type VARCHAR(30) NOT NULL,
    bill_period VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    due_date DATE NOT NULL
);

CREATE TABLE fee_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    category VARCHAR(30) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0,
    description VARCHAR(255),
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bill_id BIGINT,
    payer_name VARCHAR(50) NOT NULL,
    house VARCHAR(30) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    operator VARCHAR(50) NOT NULL,
    remark VARCHAR(255),
    paid_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parking_vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plate VARCHAR(20) NOT NULL,
    owner VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    location VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    monthly_fee DECIMAL(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    category VARCHAR(30) NOT NULL,
    audience VARCHAR(80) NOT NULL,
    publisher VARCHAR(80) NOT NULL,
    publish_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE community_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    activity_type VARCHAR(30) NOT NULL,
    location VARCHAR(120) NOT NULL,
    organizer VARCHAR(80) NOT NULL,
    start_time DATETIME NOT NULL,
    signups INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT '报名中',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE express_package (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tracking_no VARCHAR(60) NOT NULL UNIQUE,
    recipient_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    pickup_code VARCHAR(20) NOT NULL,
    cabinet_no VARCHAR(30),
    status VARCHAR(20) NOT NULL DEFAULT '待领取',
    arrived_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    picked_at DATETIME
);

CREATE TABLE access_control (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_name VARCHAR(80) NOT NULL,
    gate_name VARCHAR(80) NOT NULL,
    device_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT '在线',
    manager VARCHAR(50) NOT NULL,
    last_check_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patrol_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    route_name VARCHAR(80) NOT NULL,
    area VARCHAR(120) NOT NULL,
    assignee VARCHAR(50) NOT NULL,
    plan_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT '待巡检',
    result VARCHAR(255),
    finished_at DATETIME
);

CREATE TABLE monthly_revenue (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    month_label VARCHAR(20) NOT NULL,
    amount DECIMAL(10,2) NOT NULL
);

CREATE TABLE todo_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    deadline VARCHAR(50) NOT NULL,
    level VARCHAR(20) NOT NULL,
    completed TINYINT(1) NOT NULL DEFAULT 0
);
