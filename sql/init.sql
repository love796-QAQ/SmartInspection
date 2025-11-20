-- Database Initialization Script for Smart Inspection System

CREATE DATABASE IF NOT EXISTS smart_inspection DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE smart_inspection;

-- 1. System Tables
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    dept_id BIGINT,
    role_id BIGINT,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    ancestors VARCHAR(200),
    dept_type TINYINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Base Config Tables
DROP TABLE IF EXISTS base_inspection_category;
CREATE TABLE base_inspection_category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    sort_order INT DEFAULT 0
);

DROP TABLE IF EXISTS base_inspection_level;
CREATE TABLE base_inspection_level (
    level_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    level_name VARCHAR(50) NOT NULL,
    level_code VARCHAR(20) NOT NULL,
    color VARCHAR(20),
    score_weight INT DEFAULT 1,
    punish_rule_json TEXT,
    is_enabled TINYINT DEFAULT 1
);

DROP TABLE IF EXISTS base_inspection_item;
CREATE TABLE base_inspection_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(200) NOT NULL,
    category_id BIGINT NOT NULL,
    default_level_id BIGINT NOT NULL,
    standard_desc TEXT,
    standard_img VARCHAR(255),
    is_required TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS base_template;
CREATE TABLE base_template (
    template_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(100) NOT NULL,
    dept_id BIGINT,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS base_template_item;
CREATE TABLE base_template_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    sort_order INT DEFAULT 0
);

-- 3. Business Tables
DROP TABLE IF EXISTS biz_task;
CREATE TABLE biz_task (
    task_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(100) NOT NULL,
    template_id BIGINT,
    dept_id BIGINT NOT NULL,
    inspector_id BIGINT,
    status TINYINT DEFAULT 0,
    start_time DATETIME,
    deadline DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS biz_task_item;
CREATE TABLE biz_task_item (
    task_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    original_item_id BIGINT,
    item_name VARCHAR(200),
    category_id BIGINT,
    level_id BIGINT,
    standard_desc TEXT,
    sort_order INT
);

DROP TABLE IF EXISTS biz_record;
CREATE TABLE biz_record (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    inspector_id BIGINT NOT NULL,
    record_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    gps_location VARCHAR(100),
    device_info VARCHAR(100),
    is_offline_submission TINYINT DEFAULT 0
);

DROP TABLE IF EXISTS biz_record_detail;
CREATE TABLE biz_record_detail (
    detail_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_id BIGINT NOT NULL,
    task_item_id BIGINT NOT NULL,
    is_pass TINYINT NOT NULL,
    actual_level_id BIGINT,
    image_url VARCHAR(500),
    comment VARCHAR(500)
);

DROP TABLE IF EXISTS biz_issue;
CREATE TABLE biz_issue (
    issue_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_detail_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    responsible_user_id BIGINT,
    status TINYINT DEFAULT 0,
    rectify_deadline DATETIME,
    rectify_image VARCHAR(500),
    rectify_desc TEXT,
    verify_user_id BIGINT,
    verify_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS biz_punish_suggestion;
CREATE TABLE biz_punish_suggestion (
    suggestion_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    dept_id BIGINT,
    trigger_source VARCHAR(50),
    trigger_ref_id BIGINT,
    suggested_level_id BIGINT,
    suggested_amount DECIMAL(10,2),
    reason VARCHAR(255),
    status TINYINT DEFAULT 0,
    manager_comment VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS biz_punish_record;
CREATE TABLE biz_punish_record (
    punish_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    suggestion_id BIGINT,
    user_id BIGINT NOT NULL,
    final_amount DECIMAL(10,2),
    confirm_user_id BIGINT,
    confirm_time DATETIME
);

DROP TABLE IF EXISTS biz_performance_result;
CREATE TABLE biz_performance_result (
    result_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    dept_id BIGINT,
    period VARCHAR(20),
    total_inspections INT,
    total_issues INT,
    l1_issues INT,
    l2_issues INT,
    l3_issues INT,
    original_score DECIMAL(10,2),
    deduction_score DECIMAL(10,2),
    final_score DECIMAL(10,2),
    status INT DEFAULT 0,
    confirm_user_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Initial Data
INSERT INTO sys_user (username, password, real_name, status) VALUES ('admin', '123456', 'System Admin', 1);

INSERT INTO base_inspection_category (category_name, sort_order) VALUES 
('Personnel', 1), ('Equipment', 2), ('Food', 3), ('Process', 4), ('Hygiene', 5);

INSERT INTO base_inspection_level (level_name, level_code, color, score_weight, punish_rule_json) VALUES 
('Critical', 'L1', '#FF0000', 10, '{"consecutive": 3, "amount": 50}'),
('Major', 'L2', '#FFA500', 5, '{"consecutive": 5, "amount": 20}'),
('Minor', 'L3', '#FFFF00', 2, '{"consecutive": 10, "amount": 5}');
