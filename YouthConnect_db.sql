USE youth_connect;

-- ===========================================
-- 1. 初始化用户模块数据
-- ===========================================
INSERT INTO `user` (`phone`, `email`, `password`, `nickname`, `avatar`, `bio`, `gender`, `birthday`, `status`)
VALUES
    ('13800000001', 'alice@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Alice', '/avatar/alice.jpg', '热爱运动，喜欢交朋友', 2, '1998-05-12', 1),
    ('13800000002', 'bob@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Bob', '/avatar/bob.jpg', '摄影爱好者', 1, '1996-11-03', 1),
    ('13800000003', 'charlie@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Charlie', '/avatar/charlie.jpg', '喜欢旅行与冒险', 1, '1999-08-20', 1),
    ('13800000004', 'daisy@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Daisy', '/avatar/daisy.jpg', '音乐是我的生活', 2, '2000-02-18', 1),
    ('13800000005', 'eve@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'Eve', '/avatar/eve.jpg', '电竞女孩', 2, '1997-12-25', 1);

-- 用户兴趣
INSERT INTO `user_interest` (`user_id`, `tag`) VALUES
                                                   (1, '篮球'), (1, '旅行'), (1, '交友'),
                                                   (2, '摄影'), (2, '咖啡'), (2, '健身'),
                                                   (3, '徒步'), (3, '冒险'),
                                                   (4, '音乐'), (4, '唱歌'), (4, '吉他'),
                                                   (5, '游戏'), (5, '电竞');

-- 好友关系
INSERT INTO `friendship` (`user_id`, `friend_id`, `status`) VALUES
                                                                (1, 2, 1), (2, 1, 1),
                                                                (1, 3, 1), (3, 1, 1),
                                                                (2, 4, 0), -- 待确认
                                                                (4, 5, 1), (5, 4, 1);

-- 用户等级
INSERT INTO `user_level` (`user_id`, `points`, `level`) VALUES
                                                            (1, 1200, 5),
                                                            (2, 800, 4),
                                                            (3, 600, 3),
                                                            (4, 1500, 6),
                                                            (5, 300, 2);

-- ===========================================
-- 2. 动态社交初始化数据
-- ===========================================
INSERT INTO `post` (`user_id`, `content`, `media_type`, `media_url`, `status`)
VALUES
    (1, '今天参加了一次篮球比赛，超开心！', 1, '/images/post1.jpg', 1),
    (2, '新买的相机效果不错，拍了一些照片。', 1, '/images/post2.jpg', 1),
    (3, '刚从川藏线回来，风景美极了！', 2, '/videos/travel1.mp4', 1),
    (4, '和朋友们一起在公园唱歌，好嗨！', 0, NULL, 1),
    (5, '今晚五排冲击王者！', 0, NULL, 1);

-- 点赞
INSERT INTO `post_like` (`post_id`, `user_id`)
VALUES
    (1, 2), (1, 3), (1, 4),
    (2, 1), (2, 5),
    (3, 1), (3, 4), (3, 5);

-- 评论
INSERT INTO `post_comment` (`post_id`, `user_id`, `content`)
VALUES
    (1, 2, '太棒了！'),
    (1, 3, '加油！下次一起'),
    (3, 5, '羡慕！想去旅行'),
    (2, 1, '拍得很好，教教我！');

-- 收藏
INSERT INTO `post_collect` (`post_id`, `user_id`)
VALUES
    (3, 1), (3, 2),
    (1, 5);

-- ===========================================
-- 3. 活动模块初始化数据
-- ===========================================
INSERT INTO `event` (`creator_id`, `title`, `description`, `location`, `start_time`, `end_time`, `max_participants`, `current_participants`, `status`)
VALUES
    (1, '周末篮球友谊赛', '大家一起打篮球，锻炼身体，认识新朋友。', '重庆市体育中心', '2025-10-05 15:00:00', '2025-10-05 18:00:00', 10, 5, 1),
    (2, '摄影外拍活动', '一起去洪崖洞拍照，记录美好瞬间。', '重庆市洪崖洞', '2025-10-06 09:00:00', '2025-10-06 12:00:00', 15, 6, 1),
    (4, '音乐Live小聚', '民谣吉他分享，适合爱音乐的小伙伴。', '重庆市江北区某咖啡馆', '2025-10-07 19:00:00', '2025-10-07 21:00:00', 8, 2, 0);

-- 报名
INSERT INTO `event_signup` (`event_id`, `user_id`, `sign_status`)
VALUES
    (1, 2, 1),
    (1, 3, 1),
    (1, 4, 0),
    (1, 5, 0),
    (2, 1, 0),
    (2, 4, 0),
    (2, 5, 0);

-- 活动讨论
INSERT INTO `event_discussion` (`event_id`, `user_id`, `content`)
VALUES
    (1, 2, '比赛记得带水！'),
    (1, 3, '我提前15分钟到。'),
    (2, 1, '洪崖洞真好拍！');

-- ===========================================
-- 4. 聊天室模块初始化数据
-- ===========================================
INSERT INTO `chat_room` (`event_id`, `name`)
VALUES
    (1, '周末篮球友谊赛聊天室'),
    (2, '摄影外拍活动聊天室'),
    (3, '音乐Live小聚聊天室');

-- 成员
INSERT INTO `chat_member` (`room_id`, `user_id`)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
    (2, 1), (2, 2), (2, 4), (2, 5),
    (3, 4), (3, 5);

-- 消息
INSERT INTO `chat_message` (`room_id`, `sender_id`, `content`, `type`)
VALUES
    (1, 2, '大家下午三点见！', 0),
    (1, 3, '记得带篮球哦！', 0),
    (2, 1, '洪崖洞集合！', 0),
    (2, 5, '带上我的单反！', 0);

-- ===========================================
-- 5. AI 审核模块初始化数据
-- ===========================================
INSERT INTO `audit_task` (`task_type`, `target_id`, `status`, `result`)
VALUES
    (1, 1, 1, '动态内容正常'),
    (1, 3, 1, '视频审核通过'),
    (2, 2, 1, '活动标题健康'),
    (3, 1, 1, '聊天消息合规');

-- ===========================================
-- 6. 消息通知模块初始化数据
-- ===========================================
INSERT INTO `notification` (`user_id`, `title`, `content`, `type`, `status`)
VALUES
    (1, '系统提醒', '您的动态《篮球比赛》已通过审核', 2, 0),
    (2, '活动提醒', '摄影外拍活动将于30分钟后开始', 1, 0),
    (3, '好友申请', 'Alice请求加你为好友', 0, 1),
    (4, '系统提醒', '您已成功报名音乐Live小聚', 1, 0);

-- ===========================================
-- 7. 用户行为日志初始化
-- ===========================================
INSERT INTO `user_behavior` (`user_id`, `behavior_type`, `target_id`)
VALUES
    (1, 1, 1), -- 浏览动态
    (2, 2, 1), -- 点赞动态
    (3, 3, 1), -- 报名活动
    (4, 2, 3), -- 点赞动态
    (5, 3, 2), -- 报名活动
    (1, 4, 2); -- 分享活动

-- ===========================================
-- 验证数据完整性
-- ===========================================
-- 查询某个活动的所有聊天室成员
-- SELECT u.nickname FROM chat_member cm JOIN user u ON cm.user_id = u.id WHERE cm.room_id = 1;

-- 查询某用户的报名活动
-- SELECT e.title FROM event_signup es JOIN event e ON es.event_id = e.id WHERE es.user_id = 2;
