package com.youthconnect.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youthconnect.event.dto.EventQueryDTO;
import com.youthconnect.event.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动Mapper接口
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {

    /**
     * 分页查询活动列表
     */
    IPage<Event> selectEventPage(Page<Event> page, @Param("query") EventQueryDTO query);

    /**
     * 根据关键词搜索活动
     */
    List<Event> searchEvents(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 查询附近活动
     */
    List<Event> selectNearbyEvents(@Param("latitude") Double latitude, 
                                  @Param("longitude") Double longitude, 
                                  @Param("radius") Double radius,
                                  @Param("limit") Integer limit);

    /**
     * 根据分类查询活动
     */
    List<Event> selectEventsByCategory(@Param("categoryId") Integer categoryId, @Param("limit") Integer limit);

    /**
     * 查询精选活动
     */
    List<Event> selectFeaturedEvents(@Param("limit") Integer limit);

    /**
     * 查询热门活动
     */
    List<Event> selectPopularEvents(@Param("limit") Integer limit);

    /**
     * 根据创建者查询活动
     */
    List<Event> selectEventsByCreator(@Param("creatorId") Long creatorId);

    /**
     * 根据参与者查询活动
     */
    List<Event> selectEventsByParticipant(@Param("userId") Long userId);
}
