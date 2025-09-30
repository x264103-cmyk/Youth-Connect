package com.youthconnect.userservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youthconnect.userservice.dto.*;
import com.youthconnect.userservice.entity.PrivateMessage;
import com.youthconnect.userservice.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    UserDTO register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     */
    String login(UserLoginDTO loginDTO);

    /**
     * 根据ID获取用户信息
     */
    UserDTO getUserById(Long id);

    /**
     * 根据手机号或邮箱获取用户信息
     */
    UserDTO getUserByAccount(String account);

    /**
     * 更新用户信息
     */
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * 分页查询用户列表
     */
    IPage<UserDTO> getUserPage(UserQueryDTO queryDTO);

    /**
     * 搜索用户
     */
    List<UserDTO> searchUsers(String keyword, Integer limit);

    /**
     * 查询附近的人
     */
    List<UserDTO> getNearbyUsers(Double latitude, Double longitude, Double radius, Integer limit);

    /**
     * 根据兴趣标签查询用户
     */
    List<UserDTO> getUsersByInterest(String interest, Integer limit);

    /**
     * 添加好友
     */
    boolean addFriend(Long userId, Long friendId, String remark);

    /**
     * 处理好友申请
     */
    boolean handleFriendRequest(Long userId, Long friendId, Integer status);

    /**
     * 获取好友列表
     */
    List<UserDTO> getFriends(Long userId);

    /**
     * 获取好友申请列表
     */
    List<UserDTO> getFriendRequests(Long userId);

    /**
     * 删除好友
     */
    boolean deleteFriend(Long userId, Long friendId);

    /**
     * 更新用户等级
     */
    boolean updateUserLevel(Long userId, Integer points);

    /**
     * 获取用户等级信息
     */
    UserLevelDTO getUserLevel(Long userId);

    /**
     * 发送私信
     */
    boolean sendPrivateMessage(Long senderId, Long receiverId, String content, Integer type, String fileUrl);

    /**
     * 获取私信列表
     */
    List<PrivateMessage> getPrivateMessages(Long userId, Long friendId, Integer pageNum, Integer pageSize);

    /**
     * 标记私信为已读
     */
    boolean markMessageAsRead(Long messageId);

    /**
     * 获取未读私信数量
     */
    Integer getUnreadMessageCount(Long userId);

    /**
     * 检查用户是否存在
     */
    boolean checkUserExists(String account);

    /**
     * 验证密码
     */
    boolean validatePassword(Long userId, String password);

    /**
     * 更新密码
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    boolean resetPassword(String account, String newPassword, String smsCode);

    /**
     * 更新最后登录信息
     */
    boolean updateLastLogin(Long userId, String ipAddress);
}
