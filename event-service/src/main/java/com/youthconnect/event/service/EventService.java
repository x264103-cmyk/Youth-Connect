package com.youthconnect.event.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youthconnect.event.dto.*;
import com.youthconnect.event.entity.Event;
import com.youthconnect.event.entity.EventCategory;
import com.youthconnect.event.entity.EventSignup;

import java.util.List;

/**
 * 活动服务接口
 */
public interface EventService extends IService<Event> {

    /**
     * 创建活动
     */
    EventDTO createEvent(Long userId, EventCreateDTO createDTO);

    /**
     * 根据ID获取活动详情
     */
    EventDTO getEventById(Long id);

    /**
     * 更新活动
     */
    EventDTO updateEvent(Long userId, Long eventId, EventCreateDTO createDTO);

    /**
     * 删除活动
     */
    boolean deleteEvent(Long userId, Long eventId);

    /**
     * 分页查询活动列表
     */
    IPage<EventDTO> getEventPage(EventQueryDTO queryDTO);

    /**
     * 搜索活动
     */
    List<EventDTO> searchEvents(String keyword, Integer limit);

    /**
     * 查询附近活动
     */
    List<EventDTO> getNearbyEvents(Double latitude, Double longitude, Double radius, Integer limit);

    /**
     * 根据分类查询活动
     */
    List<EventDTO> getEventsByCategory(Integer categoryId, Integer limit);

    /**
     * 获取精选活动
     */
    List<EventDTO> getFeaturedEvents(Integer limit);

    /**
     * 获取热门活动
     */
    List<EventDTO> getPopularEvents(Integer limit);

    /**
     * 活动报名
     */
    boolean signupEvent(Long userId, EventSignupDTO signupDTO);

    /**
     * 取消报名
     */
    boolean cancelSignup(Long userId, Long eventId);

    /**
     * 活动签到
     */
    boolean checkinEvent(Long userId, Long eventId, String location, Double latitude, Double longitude);

    /**
     * 获取我的活动列表
     */
    List<EventDTO> getMyEvents(Long userId, String type);

    /**
     * 获取我报名的活动列表
     */
    List<EventDTO> getMySignupEvents(Long userId);

    /**
     * 点赞活动
     */
    boolean likeEvent(Long userId, Long eventId);

    /**
     * 取消点赞
     */
    boolean unlikeEvent(Long userId, Long eventId);

    /**
     * 获取活动分类列表
     */
    List<EventCategory> getEventCategories();

    /**
     * 获取活动报名列表
     */
    List<EventSignup> getEventSignups(Long eventId);

    /**
     * 检查用户是否已报名
     */
    boolean isUserSignedUp(Long userId, Long eventId);

    /**
     * 检查用户是否已点赞
     */
    boolean isUserLiked(Long userId, Long eventId);

    /**
     * 增加活动浏览数
     */
    boolean incrementViewCount(Long eventId);

    /**
     * 更新活动状态
     */
    boolean updateEventStatus(Long eventId, Integer status, String reason);
}
