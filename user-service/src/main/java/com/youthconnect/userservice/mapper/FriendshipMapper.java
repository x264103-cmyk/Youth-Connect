package com.youthconnect.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youthconnect.userservice.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 好友关系Mapper接口
 */
@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {

    /**
     * 查询用户的好友列表
     */
    List<Friendship> selectFriendsByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 查询用户的好友申请列表
     */
    List<Friendship> selectFriendRequestsByUserId(@Param("userId") Long userId);

    /**
     * 查询两个用户之间的好友关系
     */
    Friendship selectByUserIds(@Param("userId") Long userId, @Param("friendId") Long friendId);

    /**
     * 统计用户的好友数量
     */
    int countFriendsByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 统计用户的好友申请数量
     */
    int countFriendRequestsByUserId(@Param("userId") Long userId);

    /**
     * 更新好友关系状态
     */
    int updateStatus(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("status") Integer status);

    /**
     * 删除好友关系
     */
    int deleteFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
