-- ===========================
-- 数据库：YouthConnect (优化版)
-- 目标：青年社交 + 活动约玩 + AI 审核 + 实时聊天室平台
-- 说明：MySQL 8.0+，包含完整的表结构和索引优化
-- ===========================

CREATE DATABASE IF NOT EXISTS youth_connect DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE youth_connect;

-- ===========================================
-- 1. 用户模块 user-service (优化版)
-- ===========================================
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密后)',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `bio` VARCHAR(500) COMMENT '个人简介',
    `gender` TINYINT DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
    `birthday` DATE COMMENT '生日',
    `location` VARCHAR(100) COMMENT '所在城市',
    `latitude` DECIMAL(10, 8) COMMENT '纬度',
    `longitude` DECIMAL(11, 8) COMMENT '经度',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1正常 0封禁 2待激活)',
    `last_login_time` TIMESTAMP NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45) COMMENT '最后登录IP',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

CREATE TABLE `user_interest` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `tag` VARCHAR(50) NOT NULL COMMENT '兴趣标签',
    `weight` DECIMAL(3,2) DEFAULT 1.00 COMMENT '兴趣权重',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_interest_user` (`user_id`),
    INDEX `idx_user_interest_tag` (`tag`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户兴趣标签';

CREATE TABLE `friendship` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    `status` TINYINT DEFAULT 0 COMMENT '0待确认 1已同意 2拒绝 3已删除',
    `remark` VARCHAR(50) COMMENT '备注名',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_friendship` (`user_id`, `friend_id`),
    INDEX `idx_friendship_user` (`user_id`),
    INDEX `idx_friendship_friend` (`friend_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friend_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='好友关系表';

CREATE TABLE `user_level` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `points` INT DEFAULT 0 COMMENT '积分',
    `level` INT DEFAULT 1 COMMENT '等级',
    `experience` INT DEFAULT 0 COMMENT '经验值',
    `next_level_points` INT DEFAULT 100 COMMENT '下一级所需积分',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_level` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户积分等级表';

-- 私信功能
CREATE TABLE `private_message` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `sender_id` BIGINT NOT NULL,
    `receiver_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL COMMENT '消息内容',
    `type` TINYINT DEFAULT 0 COMMENT '0文字 1图片 2语音 3文件',
    `file_url` VARCHAR(255) COMMENT '文件URL',
    `is_read` TINYINT DEFAULT 0 COMMENT '0未读 1已读',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_private_msg_sender` (`sender_id`),
    INDEX `idx_private_msg_receiver` (`receiver_id`),
    INDEX `idx_private_msg_time` (`create_time`),
    FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='私信表';

-- ===========================================
-- 2. 动态社交模块 post-service (优化版)
-- ===========================================
CREATE TABLE `post` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL COMMENT '文字内容',
    `media_type` TINYINT DEFAULT 0 COMMENT '0无 1图片 2视频 3音频',
    `media_url` VARCHAR(500) COMMENT '媒体地址(JSON格式存储多个)',
    `location` VARCHAR(100) COMMENT '发布位置',
    `latitude` DECIMAL(10, 8) COMMENT '纬度',
    `longitude` DECIMAL(11, 8) COMMENT '经度',
    `status` TINYINT DEFAULT 0 COMMENT '0待审核 1通过 2拒绝 3已删除',
    `audit_reason` VARCHAR(255) COMMENT '审核拒绝原因',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `share_count` INT DEFAULT 0 COMMENT '分享数',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `is_top` TINYINT DEFAULT 0 COMMENT '0普通 1置顶',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_post_user` (`user_id`),
    INDEX `idx_post_status` (`status`),
    INDEX `idx_post_time` (`create_time`),
    INDEX `idx_post_location` (`latitude`, `longitude`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态表';

CREATE TABLE `post_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_post_like` (`post_id`, `user_id`),
    INDEX `idx_post_like_user` (`user_id`),
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态点赞表';

CREATE TABLE `post_comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复的用户ID',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '0删除 1正常',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_post_comment_post` (`post_id`),
    INDEX `idx_post_comment_user` (`user_id`),
    INDEX `idx_post_comment_parent` (`parent_id`),
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`parent_id`) REFERENCES `post_comment`(`id`) ON DELETE CASCADE
) COMMENT='动态评论表';

CREATE TABLE `post_collect` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_post_collect` (`post_id`, `user_id`),
    INDEX `idx_post_collect_user` (`user_id`),
    FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态收藏表';

-- ===========================================
-- 3. 活动模块 event-service (优化版)
-- ===========================================
CREATE TABLE `event_category` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `icon` VARCHAR(255) COMMENT '分类图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT='活动分类表';

CREATE TABLE `event` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `creator_id` BIGINT NOT NULL COMMENT '活动发起人',
    `category_id` INT COMMENT '活动分类',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `description` TEXT COMMENT '描述',
    `cover_image` VARCHAR(255) COMMENT '封面图片',
    `location` VARCHAR(255) COMMENT '地点',
    `latitude` DECIMAL(10, 8) COMMENT '纬度',
    `longitude` DECIMAL(11, 8) COMMENT '经度',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `registration_deadline` DATETIME COMMENT '报名截止时间',
    `max_participants` INT NOT NULL COMMENT '人数上限',
    `current_participants` INT DEFAULT 0 COMMENT '当前报名人数',
    `price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '活动费用',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `contact_wechat` VARCHAR(50) COMMENT '联系微信',
    `tags` VARCHAR(200) COMMENT '活动标签(逗号分隔)',
    `status` TINYINT DEFAULT 0 COMMENT '0待审核 1进行中 2已满 3已结束 4已取消 5驳回',
    `audit_reason` VARCHAR(255) COMMENT '审核拒绝原因',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `is_featured` TINYINT DEFAULT 0 COMMENT '0普通 1精选',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_event_creator` (`creator_id`),
    INDEX `idx_event_category` (`category_id`),
    INDEX `idx_event_status` (`status`),
    INDEX `idx_event_time` (`start_time`),
    INDEX `idx_event_location` (`latitude`, `longitude`),
    FOREIGN KEY (`creator_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `event_category`(`id`)
) COMMENT='活动表';

CREATE TABLE `event_signup` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `event_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `sign_status` TINYINT DEFAULT 0 COMMENT '0已报名 1已签到 2已取消',
    `signup_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    `checkin_time` TIMESTAMP NULL COMMENT '签到时间',
    `checkin_location` VARCHAR(255) COMMENT '签到位置',
    `checkin_latitude` DECIMAL(10, 8) COMMENT '签到纬度',
    `checkin_longitude` DECIMAL(11, 8) COMMENT '签到经度',
    `remark` VARCHAR(200) COMMENT '报名备注',
    UNIQUE KEY `uk_event_signup` (`event_id`, `user_id`),
    INDEX `idx_event_signup_user` (`user_id`),
    INDEX `idx_event_signup_status` (`sign_status`),
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='活动报名表';

CREATE TABLE `event_discussion` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `event_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '0删除 1正常',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_event_discussion_event` (`event_id`),
    INDEX `idx_event_discussion_user` (`user_id`),
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='活动讨论评论';

-- ===========================================
-- 4. 实时聊天室模块 chat-service (优化版)
-- ===========================================
CREATE TABLE `chat_room` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `event_id` BIGINT UNIQUE NOT NULL COMMENT '一个活动对应一个聊天室',
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255) COMMENT '聊天室描述',
    `avatar` VARCHAR(255) COMMENT '聊天室头像',
    `max_members` INT DEFAULT 500 COMMENT '最大成员数',
    `current_members` INT DEFAULT 0 COMMENT '当前成员数',
    `status` TINYINT DEFAULT 1 COMMENT '0关闭 1正常 2禁言',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE
) COMMENT='聊天室表';

CREATE TABLE `chat_member` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` TINYINT DEFAULT 0 COMMENT '0普通成员 1管理员 2房主',
    `nickname` VARCHAR(50) COMMENT '在聊天室中的昵称',
    `join_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `last_read_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后阅读时间',
    UNIQUE KEY `uk_chat_member` (`room_id`, `user_id`),
    INDEX `idx_chat_member_user` (`user_id`),
    FOREIGN KEY (`room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='聊天室成员';

CREATE TABLE `chat_message` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `room_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `content` TEXT COMMENT '消息内容',
    `type` TINYINT DEFAULT 0 COMMENT '0文字 1图片 2语音 3视频 4文件 5系统消息',
    `file_url` VARCHAR(255) COMMENT '文件URL',
    `file_size` BIGINT COMMENT '文件大小(字节)',
    `reply_to_id` BIGINT DEFAULT NULL COMMENT '回复的消息ID',
    `is_recalled` TINYINT DEFAULT 0 COMMENT '0正常 1已撤回',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_chat_message_room` (`room_id`),
    INDEX `idx_chat_message_sender` (`sender_id`),
    INDEX `idx_chat_message_time` (`create_time`),
    FOREIGN KEY (`room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='聊天室消息表';

-- ===========================================
-- 5. AI 审核模块 ai-audit-service (优化版)
-- ===========================================
CREATE TABLE `audit_task` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `task_type` TINYINT NOT NULL COMMENT '1动态 2活动 3聊天消息 4评论',
    `target_id` BIGINT NOT NULL COMMENT '关联对象ID',
    `content` TEXT COMMENT '待审核内容',
    `media_url` VARCHAR(255) COMMENT '媒体文件URL',
    `status` TINYINT DEFAULT 0 COMMENT '0待审核 1通过 2拒绝 3审核中',
    `result` VARCHAR(500) COMMENT '审核结果描述',
    `confidence` DECIMAL(5,4) COMMENT '置信度',
    `audit_time` TIMESTAMP NULL COMMENT '审核时间',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_audit_task_type` (`task_type`),
    INDEX `idx_audit_task_target` (`target_id`),
    INDEX `idx_audit_task_status` (`status`)
) COMMENT='审核任务表';

CREATE TABLE `sensitive_word` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `word` VARCHAR(100) NOT NULL COMMENT '敏感词',
    `level` TINYINT DEFAULT 1 COMMENT '敏感级别(1-5)',
    `category` VARCHAR(50) COMMENT '分类',
    `status` TINYINT DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_sensitive_word` (`word`)
) COMMENT='敏感词库';

-- ===========================================
-- 6. 消息通知模块 message-service (优化版)
-- ===========================================
CREATE TABLE `notification` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '接收人',
    `title` VARCHAR(100) NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `type` TINYINT DEFAULT 0 COMMENT '0系统通知 1活动提醒 2审核结果 3好友申请 4评论回复 5点赞通知',
    `related_id` BIGINT COMMENT '关联对象ID',
    `related_type` TINYINT COMMENT '关联对象类型',
    `status` TINYINT DEFAULT 0 COMMENT '0未读 1已读',
    `is_push` TINYINT DEFAULT 0 COMMENT '0未推送 1已推送',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `read_time` TIMESTAMP NULL COMMENT '阅读时间',
    INDEX `idx_notification_user` (`user_id`),
    INDEX `idx_notification_status` (`status`),
    INDEX `idx_notification_type` (`type`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='系统通知';

-- ===========================================
-- 7. 推荐模块 recommend-service (优化版)
-- ===========================================
CREATE TABLE `user_behavior` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `behavior_type` TINYINT NOT NULL COMMENT '1浏览 2点赞 3报名 4分享 5评论 6收藏',
    `target_id` BIGINT NOT NULL COMMENT '对象ID(动态或活动)',
    `target_type` TINYINT NOT NULL COMMENT '1动态 2活动',
    `duration` INT COMMENT '停留时长(秒)',
    `device_type` TINYINT COMMENT '设备类型(1手机 2平板 3电脑)',
    `ip_address` VARCHAR(45) COMMENT 'IP地址',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_behavior_user` (`user_id`),
    INDEX `idx_user_behavior_type` (`behavior_type`),
    INDEX `idx_user_behavior_target` (`target_type`, `target_id`),
    INDEX `idx_user_behavior_time` (`create_time`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户行为日志表';

-- ===========================================
-- 8. 系统配置模块
-- ===========================================
CREATE TABLE `system_config` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_type` VARCHAR(50) COMMENT '配置类型',
    `description` VARCHAR(255) COMMENT '描述',
    `is_public` TINYINT DEFAULT 0 COMMENT '0私有 1公开',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_system_config_key` (`config_key`)
) COMMENT='系统配置表';

-- ===========================================
-- 9. 文件管理模块
-- ===========================================
CREATE TABLE `file_upload` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_url` VARCHAR(500) NOT NULL COMMENT '访问URL',
    `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `mime_type` VARCHAR(100) COMMENT 'MIME类型',
    `width` INT COMMENT '图片宽度',
    `height` INT COMMENT '图片高度',
    `duration` INT COMMENT '视频/音频时长(秒)',
    `status` TINYINT DEFAULT 1 COMMENT '0删除 1正常',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_file_upload_user` (`user_id`),
    INDEX `idx_file_upload_type` (`file_type`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='文件上传记录表';

-- ===========================================
-- 复合索引优化
-- ===========================================
-- 用户相关索引
CREATE INDEX `idx_user_phone_email` ON `user`(`phone`, `email`);
CREATE INDEX `idx_user_location` ON `user`(`latitude`, `longitude`);
CREATE INDEX `idx_user_status_time` ON `user`(`status`, `create_time`);

-- 动态相关索引
CREATE INDEX `idx_post_user_status` ON `post`(`user_id`, `status`);
CREATE INDEX `idx_post_status_time` ON `post`(`status`, `create_time`);
CREATE INDEX `idx_post_location_time` ON `post`(`latitude`, `longitude`, `create_time`);

-- 活动相关索引
CREATE INDEX `idx_event_creator_status` ON `event`(`creator_id`, `status`);
CREATE INDEX `idx_event_category_status` ON `event`(`category_id`, `status`);
CREATE INDEX `idx_event_time_status` ON `event`(`start_time`, `status`);
CREATE INDEX `idx_event_location_time` ON `event`(`latitude`, `longitude`, `start_time`);

-- 聊天相关索引
CREATE INDEX `idx_chat_message_room_time` ON `chat_message`(`room_id`, `create_time`);
CREATE INDEX `idx_chat_member_room_role` ON `chat_member`(`room_id`, `role`);

-- 通知相关索引
CREATE INDEX `idx_notification_user_status` ON `notification`(`user_id`, `status`);
CREATE INDEX `idx_notification_type_time` ON `notification`(`type`, `create_time`);

-- 行为日志相关索引
CREATE INDEX `idx_user_behavior_user_type` ON `user_behavior`(`user_id`, `behavior_type`);
CREATE INDEX `idx_user_behavior_target_type` ON `user_behavior`(`target_type`, `target_id`);

-- 完成
