-- ===========================
-- 数据库：YouthConnect
-- 目标：青年社交 + 活动约玩 + AI 审核 + 实时聊天室平台
-- 说明：MySQL 8.0+
-- ===========================

CREATE DATABASE IF NOT EXISTS youth_connect DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE youth_connect;

-- ===========================================
-- 1. 用户模块 user-service
-- ===========================================
CREATE TABLE `user` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                        `phone` VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号',
                        `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
                        `password` VARCHAR(255) NOT NULL COMMENT '密码(加密后)',
                        `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
                        `avatar` VARCHAR(255) COMMENT '头像',
                        `bio` VARCHAR(500) COMMENT '个人简介',
                        `gender` TINYINT COMMENT '性别(0未知 1男 2女)',
                        `birthday` DATE COMMENT '生日',
                        `status` TINYINT DEFAULT 1 COMMENT '状态 1正常 0封禁',
                        `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                        `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

CREATE TABLE `user_interest` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `user_id` BIGINT NOT NULL,
                                 `tag` VARCHAR(50) NOT NULL COMMENT '兴趣标签',
                                 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户兴趣标签';

CREATE TABLE `friendship` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `user_id` BIGINT NOT NULL,
                              `friend_id` BIGINT NOT NULL,
                              `status` TINYINT DEFAULT 0 COMMENT '0待确认 1已同意 2拒绝',
                              `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                              FOREIGN KEY (`friend_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='好友关系表';

CREATE TABLE `user_level` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `user_id` BIGINT NOT NULL,
                              `points` INT DEFAULT 0 COMMENT '积分',
                              `level` INT DEFAULT 1 COMMENT '等级',
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户积分等级表';

-- ===========================================
-- 2. 动态社交模块 post-service
-- ===========================================
CREATE TABLE `post` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `user_id` BIGINT NOT NULL,
                        `content` TEXT NOT NULL COMMENT '文字内容',
                        `media_type` TINYINT DEFAULT 0 COMMENT '0无 1图片 2视频',
                        `media_url` VARCHAR(255) COMMENT '媒体地址',
                        `status` TINYINT DEFAULT 0 COMMENT '0待审核 1通过 2拒绝',
                        `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态表';

CREATE TABLE `post_like` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `post_id` BIGINT NOT NULL,
                             `user_id` BIGINT NOT NULL,
                             `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             UNIQUE KEY (`post_id`, `user_id`),
                             FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
                             FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态点赞表';

CREATE TABLE `post_comment` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `post_id` BIGINT NOT NULL,
                                `user_id` BIGINT NOT NULL,
                                `content` VARCHAR(500) NOT NULL,
                                `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID',
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态评论表';

CREATE TABLE `post_collect` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `post_id` BIGINT NOT NULL,
                                `user_id` BIGINT NOT NULL,
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE KEY (`post_id`, `user_id`),
                                FOREIGN KEY (`post_id`) REFERENCES `post`(`id`) ON DELETE CASCADE,
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='动态收藏表';

-- ===========================================
-- 3. 活动模块 event-service
-- ===========================================
CREATE TABLE `event` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `creator_id` BIGINT NOT NULL COMMENT '活动发起人',
                         `title` VARCHAR(100) NOT NULL COMMENT '标题',
                         `description` TEXT COMMENT '描述',
                         `location` VARCHAR(255) COMMENT '地点',
                         `start_time` DATETIME NOT NULL COMMENT '开始时间',
                         `end_time` DATETIME NOT NULL COMMENT '结束时间',
                         `max_participants` INT NOT NULL COMMENT '人数上限',
                         `current_participants` INT DEFAULT 0 COMMENT '当前报名人数',
                         `status` TINYINT DEFAULT 0 COMMENT '0待审核 1进行中 2已满 3已结束 4驳回',
                         `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (`creator_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='活动表';

CREATE TABLE `event_signup` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `event_id` BIGINT NOT NULL,
                                `user_id` BIGINT NOT NULL,
                                `sign_status` TINYINT DEFAULT 0 COMMENT '0已报名 1已签到',
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE KEY (`event_id`, `user_id`),
                                FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE,
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='活动报名表';

CREATE TABLE `event_discussion` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    `event_id` BIGINT NOT NULL,
                                    `user_id` BIGINT NOT NULL,
                                    `content` VARCHAR(500) NOT NULL,
                                    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE,
                                    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='活动讨论评论';

-- ===========================================
-- 4. 实时聊天室模块 chat-service
-- ===========================================
CREATE TABLE `chat_room` (
                             `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `event_id` BIGINT UNIQUE NOT NULL COMMENT '一个活动对应一个聊天室',
                             `name` VARCHAR(100) NOT NULL,
                             `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (`event_id`) REFERENCES `event`(`id`) ON DELETE CASCADE
) COMMENT='聊天室表';

CREATE TABLE `chat_member` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                               `room_id` BIGINT NOT NULL,
                               `user_id` BIGINT NOT NULL,
                               `join_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               UNIQUE KEY (`room_id`, `user_id`),
                               FOREIGN KEY (`room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE,
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='聊天室成员';

CREATE TABLE `chat_message` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `room_id` BIGINT NOT NULL,
                                `sender_id` BIGINT NOT NULL,
                                `content` TEXT COMMENT '消息内容',
                                `type` TINYINT DEFAULT 0 COMMENT '0文字 1语音 2图片',
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (`room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE,
                                FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='聊天室消息表';

-- ===========================================
-- 5. AI 审核模块 ai-audit-service
-- ===========================================
CREATE TABLE `audit_task` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `task_type` TINYINT NOT NULL COMMENT '1动态 2活动 3聊天消息',
                              `target_id` BIGINT NOT NULL COMMENT '关联对象ID',
                              `status` TINYINT DEFAULT 0 COMMENT '0待审核 1通过 2拒绝',
                              `result` VARCHAR(255) COMMENT '审核结果描述',
                              `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT='审核任务表';

-- ===========================================
-- 6. 消息通知模块 message-service
-- ===========================================
CREATE TABLE `notification` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `user_id` BIGINT NOT NULL COMMENT '接收人',
                                `title` VARCHAR(100) NOT NULL,
                                `content` VARCHAR(500) NOT NULL,
                                `type` TINYINT DEFAULT 0 COMMENT '0系统通知 1活动提醒 2审核结果',
                                `status` TINYINT DEFAULT 0 COMMENT '0未读 1已读',
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='系统通知';

-- ===========================================
-- 7. 推荐模块 recommend-service (可选)
-- ===========================================
CREATE TABLE `user_behavior` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `user_id` BIGINT NOT NULL,
                                 `behavior_type` TINYINT NOT NULL COMMENT '1浏览 2点赞 3报名 4分享',
                                 `target_id` BIGINT NOT NULL COMMENT '对象ID(动态或活动)',
                                 `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) COMMENT='用户行为日志表';

-- ===========================================
-- 索引优化
-- ===========================================
CREATE INDEX idx_post_user ON post(user_id);
CREATE INDEX idx_event_creator ON event(creator_id);
CREATE INDEX idx_chat_room_event ON chat_room(event_id);
CREATE INDEX idx_chat_message_room ON chat_message(room_id);
CREATE INDEX idx_audit_task_target ON audit_task(target_id);

-- 完成
