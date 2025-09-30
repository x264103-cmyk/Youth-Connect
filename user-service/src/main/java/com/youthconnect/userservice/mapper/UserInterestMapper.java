package com.youthconnect.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youthconnect.userservice.entity.UserInterest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户兴趣标签Mapper接口
 */
@Mapper
public interface UserInterestMapper extends BaseMapper<UserInterest> {

    /**
     * 根据用户ID查询兴趣标签
     */
    List<String> selectTagsByUserId(@Param("userId") Long userId);

    /**
     * 根据兴趣标签查询用户ID列表
     */
    List<Long> selectUserIdsByTag(@Param("tag") String tag);

    /**
     * 批量插入用户兴趣标签
     */
    int batchInsert(@Param("userId") Long userId, @Param("tags") List<String> tags);

    /**
     * 根据用户ID删除所有兴趣标签
     */
    int deleteByUserId(@Param("userId") Long userId);
}
