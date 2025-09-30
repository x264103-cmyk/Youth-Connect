package com.youthconnect.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youthconnect.userservice.dto.UserQueryDTO;
import com.youthconnect.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("query") UserQueryDTO query);

    /**
     * 根据关键词搜索用户
     */
    List<User> searchUsers(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 查询附近的人
     */
    List<User> selectNearbyUsers(@Param("latitude") Double latitude, 
                                @Param("longitude") Double longitude, 
                                @Param("radius") Double radius,
                                @Param("limit") Integer limit);

    /**
     * 根据兴趣标签查询用户
     */
    List<User> selectUsersByInterest(@Param("interest") String interest, @Param("limit") Integer limit);

    /**
     * 查询用户的好友列表
     */
    List<User> selectFriendsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的好友申请列表
     */
    List<User> selectFriendRequestsByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否存在
     */
    User selectByPhoneOrEmail(@Param("account") String account);
}
