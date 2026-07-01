DROP TABLE IF EXISTS todo_item;
DROP TABLE IF EXISTS monthly_revenue;
DROP TABLE IF EXISTS notice;
DROP TABLE IF EXISTS parking_vehicle;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS repair_order;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS resident;
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
