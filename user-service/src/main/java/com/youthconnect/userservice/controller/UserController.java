package com.youthconnect.userservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youthconnect.userservice.common.Result;
import com.youthconnect.userservice.dto.*;
import com.youthconnect.userservice.entity.PrivateMessage;
import com.youthconnect.userservice.entity.User;
import com.youthconnect.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            UserDTO userDTO = userService.register(registerDTO);
            return Result.success(userDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }



    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            return Result.success(token);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserDTO> getCurrentUserInfo() {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码，实际应该从JWT token解析
            UserDTO userDTO = userService.getUserById(userId);
            if (userDTO == null) {
                return Result.error("用户不存在");
            }
            return Result.success(userDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            return Result.error("用户不存在");
        }
        return Result.success(userDTO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新当前用户信息
     */
    @PutMapping("/profile")
    public Result<UserDTO> updateCurrentUserProfile(@RequestBody UserDTO userDTO) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码，实际应该从JWT token解析
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码，实际应该从JWT token解析
            
            // 简单的文件上传处理，实际项目中应该上传到云存储
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }
            
            // 检查文件大小 (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return Result.error("文件大小不能超过5MB");
            }
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // String fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + extension;
            
            // 这里应该保存到云存储，暂时返回模拟URL
            String avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=" + userId;
            
            // 更新用户头像
            User user = userService.getById(userId);
            if (user != null) {
                user.setAvatar(avatarUrl);
                userService.updateById(user);
            }
            
            return Result.success(avatarUrl);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    public Result<IPage<UserDTO>> getUserPage(@RequestBody UserQueryDTO queryDTO) {
        IPage<UserDTO> userPage = userService.getUserPage(queryDTO);
        return Result.success(userPage);
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public Result<List<UserDTO>> searchUsers(@RequestParam String keyword, 
                                           @RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.searchUsers(keyword, limit);
        return Result.success(users);
    }

    /**
     * 查询附近的人
     */
    @GetMapping("/nearby")
    public Result<List<UserDTO>> getNearbyUsers(@RequestParam Double latitude,
                                              @RequestParam Double longitude,
                                              @RequestParam(defaultValue = "10.0") Double radius,
                                              @RequestParam(defaultValue = "20") Integer limit) {
        List<UserDTO> users = userService.getNearbyUsers(latitude, longitude, radius, limit);
        return Result.success(users);
    }

    /**
     * 根据兴趣标签查询用户
     */
    @GetMapping("/interest")
    public Result<List<UserDTO>> getUsersByInterest(@RequestParam String interest,
                                                   @RequestParam(defaultValue = "20") Integer limit) {
        List<UserDTO> users = userService.getUsersByInterest(interest, limit);
        return Result.success(users);
    }

    /**
     * 添加好友
     */
    @PostMapping("/friend")
    public Result<Boolean> addFriend(@RequestParam Long friendId, 
                                   @RequestParam(required = false) String remark) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = userService.addFriend(userId, friendId, remark);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 处理好友申请
     */
    @PutMapping("/friend/{friendId}")
    public Result<Boolean> handleFriendRequest(@PathVariable Long friendId, 
                                             @RequestParam Integer status) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = userService.handleFriendRequest(userId, friendId, status);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取好友列表
     */
    @GetMapping("/friends")
    public Result<List<UserDTO>> getFriends() {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        List<UserDTO> friends = userService.getFriends(userId);
        return Result.success(friends);
    }

    /**
     * 获取好友申请列表
     */
    @GetMapping("/friend-requests")
    public Result<List<UserDTO>> getFriendRequests() {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        List<UserDTO> requests = userService.getFriendRequests(userId);
        return Result.success(requests);
    }

    /**
     * 删除好友
     */
    @DeleteMapping("/friend/{friendId}")
    public Result<Boolean> deleteFriend(@PathVariable Long friendId) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = userService.deleteFriend(userId, friendId);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户等级信息
     */
    @GetMapping("/{id}/level")
    public Result<UserLevelDTO> getUserLevel(@PathVariable Long id) {
        UserLevelDTO userLevel = userService.getUserLevel(id);
        return Result.success(userLevel);
    }

    /**
     * 发送私信
     */
    @PostMapping("/message")
    public Result<Boolean> sendPrivateMessage(@RequestParam Long receiverId,
                                            @RequestParam String content,
                                            @RequestParam(defaultValue = "0") Integer type,
                                            @RequestParam(required = false) String fileUrl) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long senderId = 1L; // 临时硬编码
            boolean success = userService.sendPrivateMessage(senderId, receiverId, content, type, fileUrl);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取私信列表
     */
    @GetMapping("/message")
    public Result<List<PrivateMessage>> getPrivateMessages(@RequestParam Long friendId,
                                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                                           @RequestParam(defaultValue = "20") Integer pageSize) {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        List<PrivateMessage> messages =  userService.getPrivateMessages(userId, friendId, pageNum, pageSize);
        return Result.success(messages);
    }

    /**
     * 标记私信为已读
     */
    @PutMapping("/message/{messageId}/read")
    public Result<Boolean> markMessageAsRead(@PathVariable Long messageId) {
        try {
            boolean success = userService.markMessageAsRead(messageId);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取未读私信数量
     */
    @GetMapping("/message/unread-count")
    public Result<Integer> getUnreadMessageCount() {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        Integer count = userService.getUnreadMessageCount(userId);
        return Result.success(count);
    }

    /**
     * 更新密码
     */
    @PutMapping("/password")
    public Result<Boolean> updatePassword(@RequestParam String oldPassword,
                                        @RequestParam String newPassword) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("/password/reset")
    public Result<Boolean> resetPassword(@RequestParam String account,
                                       @RequestParam String newPassword,
                                       @RequestParam String smsCode) {
        try {
            boolean success = userService.resetPassword(account, newPassword, smsCode);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
