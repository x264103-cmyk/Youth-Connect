package com.youthconnect.userservice.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youthconnect.userservice.common.JwtUtil;
import com.youthconnect.userservice.dto.*;
import com.youthconnect.userservice.entity.*;
import com.youthconnect.userservice.mapper.FriendshipMapper;
import com.youthconnect.userservice.mapper.UserInterestMapper;
import com.youthconnect.userservice.mapper.UserMapper;
import com.youthconnect.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInterestMapper userInterestMapper;

    @Autowired
    private FriendshipMapper friendshipMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO register(UserRegisterDTO registerDTO) {
        // 检查用户是否已存在
        if (checkUserExists(registerDTO.getPhone())) {
            throw new RuntimeException("手机号已注册");
        }
        if (StringUtils.hasText(registerDTO.getEmail()) && checkUserExists(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已注册");
        }

        // 验证密码
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }

        // 创建用户
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setGender(registerDTO.getGender());
        user.setBirthday(registerDTO.getBirthday());
        user.setLocation(registerDTO.getLocation());
        user.setBio(registerDTO.getBio());
        user.setStatus(1); // 正常状态

        // 保存用户
        save(user);

        // 保存兴趣标签
        if (!CollectionUtils.isEmpty(registerDTO.getInterests())) {
            List<UserInterest> interests = registerDTO.getInterests().stream()
                    .map(tag -> {
                        UserInterest interest = new UserInterest();
                        interest.setUserId(user.getId());
                        interest.setTag(tag);
                        interest.setWeight(BigDecimal.valueOf(1.0));
                        return interest;
                    })
                    .collect(Collectors.toList());
            // 批量插入兴趣标签
            for (String interest : registerDTO.getInterests()) {
                UserInterest userInterest = new UserInterest();
                userInterest.setUserId(user.getId());
                userInterest.setTag(interest);
                userInterest.setWeight(BigDecimal.valueOf(1.0));
                userInterestMapper.insert(userInterest);
            }
        }

        // 初始化用户等级
        UserLevel userLevel = new UserLevel();
        userLevel.setUserId(user.getId());
        userLevel.setPoints(0);
        userLevel.setLevel(1);
        userLevel.setExperience(0);
        userLevel.setNextLevelPoints(100);
        // 这里应该保存到数据库，但暂时注释掉
        // userLevelMapper.insert(userLevel);

        return convertToUserDTO(user);
    }

    @Override
    public String login(UserLoginDTO loginDTO) {
        // 根据账号查找用户
        User user = userMapper.selectByPhoneOrEmail(loginDTO.getAccount());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被封禁");
        }

        // 更新最后登录信息
        updateLastLogin(user.getId(), "127.0.0.1");

        // 生成JWT token (这里简化处理，实际应该使用JWT工具类)
        // 自定义秘钥
        String key = "youth-connect-secret";

        // 自定义负载信息
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        payload.put("expire_time", System.currentTimeMillis() + 3600_000); // 1小时后过期
        // 生成 JWT Token
        return JWTUtil.createToken(payload, key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserByAccount(String account) {
        User user = userMapper.selectByPhoneOrEmail(account);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        BeanUtils.copyProperties(userDTO, user, "id", "phone", "password", "createTime");
        updateById(user);

        // 更新兴趣标签
        if (!CollectionUtils.isEmpty(userDTO.getInterests())) {
            // 删除原有兴趣标签
            QueryWrapper<UserInterest> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", id);
            userInterestMapper.delete(wrapper);
            
            // 插入新的兴趣标签
            for (String interest : userDTO.getInterests()) {
                UserInterest userInterest = new UserInterest();
                userInterest.setUserId(id);
                userInterest.setTag(interest);
                userInterest.setWeight(BigDecimal.valueOf(1.0));
                userInterestMapper.insert(userInterest);
            }
        }

        return convertToUserDTO(user);
    }

    @Override
    public IPage<UserDTO> getUserPage(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<User> userPage = userMapper.selectUserPage(page, queryDTO);
        
        return userPage.convert(this::convertToUserDTO);
    }

    @Override
    public List<UserDTO> searchUsers(String keyword, Integer limit) {
        List<User> users = userMapper.searchUsers(keyword, limit);
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getNearbyUsers(Double latitude, Double longitude, Double radius, Integer limit) {
        List<User> users = userMapper.selectNearbyUsers(latitude, longitude, radius, limit);
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByInterest(String interest, Integer limit) {
        List<User> users = userMapper.selectUsersByInterest(interest, limit);
        return users.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addFriend(Long userId, Long friendId, String remark) {
        // 检查是否已经是好友
        Friendship existing = friendshipMapper.selectByUserIds(userId, friendId);
        if (existing != null) {
            throw new RuntimeException("已经是好友关系");
        }

        // 创建好友申请
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(0); // 待确认
        friendship.setRemark(remark);
        friendshipMapper.insert(friendship);

        return true;
    }

    @Override
    @Transactional
    public boolean handleFriendRequest(Long userId, Long friendId, Integer status) {
        Friendship friendship = friendshipMapper.selectByUserIds(friendId, userId);
        if (friendship == null) {
            throw new RuntimeException("好友申请不存在");
        }

        if (status == 1) {
            // 同意申请，创建双向好友关系
            friendship.setStatus(1);
            friendshipMapper.updateById(friendship);

            // 创建反向好友关系
            Friendship reverseFriendship = new Friendship();
            reverseFriendship.setUserId(userId);
            reverseFriendship.setFriendId(friendId);
            reverseFriendship.setStatus(1);
            reverseFriendship.setRemark("");
            friendshipMapper.insert(reverseFriendship);
        } else {
            // 拒绝申请
            friendship.setStatus(2);
            friendshipMapper.updateById(friendship);
        }

        return true;
    }

    @Override
    public List<UserDTO> getFriends(Long userId) {
        List<Friendship> friendships = friendshipMapper.selectFriendsByUserId(userId, 1);
        List<Long> friendIds = friendships.stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(friendIds)) {
            return new ArrayList<>();
        }

        List<User> friends = listByIds(friendIds);
        return friends.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getFriendRequests(Long userId) {
        List<Friendship> requests = friendshipMapper.selectFriendRequestsByUserId(userId);
        List<Long> requestUserIds = requests.stream()
                .map(Friendship::getUserId)
                .collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(requestUserIds)) {
            return new ArrayList<>();
        }

        List<User> requestUsers = listByIds(requestUserIds);
        return requestUsers.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteFriend(Long userId, Long friendId) {
        // 删除双向好友关系
        friendshipMapper.deleteFriendship(userId, friendId);
        friendshipMapper.deleteFriendship(friendId, userId);
        return true;
    }

    @Override
    public boolean updateUserLevel(Long userId, Integer points) {
        // 这里应该实现等级计算逻辑
        return true;
    }

    @Override
    public UserLevelDTO getUserLevel(Long userId) {
        // 这里应该查询用户等级信息
        UserLevelDTO levelDTO = new UserLevelDTO();
        levelDTO.setPoints(0);
        levelDTO.setLevel(1);
        levelDTO.setExperience(0);
        levelDTO.setNextLevelPoints(100);
        levelDTO.setLevelName("新手");
        return levelDTO;
    }

    @Override
    public boolean sendPrivateMessage(Long senderId, Long receiverId, String content, Integer type, String fileUrl) {
        PrivateMessage message = new PrivateMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setType(type);
        message.setFileUrl(fileUrl);
        message.setIsRead(0);
        // 这里应该保存到数据库
        return true;
    }

    @Override
    public List<PrivateMessage> getPrivateMessages(Long userId, Long friendId, Integer pageNum, Integer pageSize) {



        // 这里应该查询私信列表
        return new ArrayList<PrivateMessage>();
    }

    @Override
    public boolean markMessageAsRead(Long messageId) {
        // 这里应该标记消息为已读
        return true;
    }

    @Override
    public Integer getUnreadMessageCount(Long userId) {
        // 这里应该查询未读消息数量
        return 0;
    }

    @Override
    public boolean checkUserExists(String account) {
        User user = userMapper.selectByPhoneOrEmail(account);
        return user != null;
    }

    @Override
    public boolean validatePassword(Long userId, String password) {
        User user = getById(userId);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    @Transactional
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        return true;
    }

    @Override
    @Transactional
    public boolean resetPassword(String account, String newPassword, String smsCode) {
        // 这里应该验证短信验证码
        User user = userMapper.selectByPhoneOrEmail(account);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        return true;
    }

    @Override
    public boolean updateLastLogin(Long userId, String ipAddress) {
        User user = getById(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        updateById(user);
        return true;
    }

    /**
     * 转换为UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        
        // 设置兴趣标签
        List<String> interests = userInterestMapper.selectTagsByUserId(user.getId());
        userDTO.setInterests(interests);
        
        // 设置用户等级
        userDTO.setUserLevel(getUserLevel(user.getId()));
        
        // 设置好友数量
        Integer friendCount = friendshipMapper.countFriendsByUserId(user.getId(), 1);
        userDTO.setFriendCount(friendCount);
        
        // 确保所有必要字段都有值
        if (userDTO.getNickname() == null) {
            userDTO.setNickname("用户" + user.getId());
        }
        if (userDTO.getBio() == null) {
            userDTO.setBio("");
        }
        if (userDTO.getGender() == null) {
            userDTO.setGender(0);
        }
        if (userDTO.getLocation() == null) {
            userDTO.setLocation("");
        }
        if (userDTO.getInterests() == null) {
            userDTO.setInterests(new ArrayList<>());
        }
        
        return userDTO;
    }
}
