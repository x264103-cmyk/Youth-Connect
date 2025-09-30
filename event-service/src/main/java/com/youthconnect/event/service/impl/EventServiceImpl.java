package com.youthconnect.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youthconnect.event.dto.*;
import com.youthconnect.event.entity.Event;
import com.youthconnect.event.entity.EventCategory;
import com.youthconnect.event.entity.EventSignup;
import com.youthconnect.event.mapper.EventMapper;
import com.youthconnect.event.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务实现类
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Override
    @Transactional
    public EventDTO createEvent(Long userId, EventCreateDTO createDTO) {
        Event event = new Event();
        BeanUtils.copyProperties(createDTO, event);
        event.setCreatorId(userId);
        event.setStatus(0); // 待审核
        event.setViewCount(0);
        event.setLikeCount(0);
        event.setIsFeatured(0);
        event.setCurrentParticipants(0);
        
        // 处理标签
        if (!CollectionUtils.isEmpty(createDTO.getTags())) {
            event.setTags(String.join(",", createDTO.getTags()));
        }

        save(event);
        return convertToEventDTO(event);
    }

    @Override
    public EventDTO getEventById(Long id) {
        Event event = getById(id);
        if (event == null) {
            return null;
        }
        
        // 增加浏览数
        incrementViewCount(id);
        
        return convertToEventDTO(event);
    }

    @Override
    @Transactional
    public EventDTO updateEvent(Long userId, Long eventId, EventCreateDTO createDTO) {
        Event event = getById(eventId);
        if (event == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (!event.getCreatorId().equals(userId)) {
            throw new RuntimeException("无权限修改此活动");
        }

        BeanUtils.copyProperties(createDTO, event, "id", "creatorId", "createTime");
        
        // 处理标签
        if (!CollectionUtils.isEmpty(createDTO.getTags())) {
            event.setTags(String.join(",", createDTO.getTags()));
        }
        
        updateById(event);
        return convertToEventDTO(event);
    }

    @Override
    @Transactional
    public boolean deleteEvent(Long userId, Long eventId) {
        Event event = getById(eventId);
        if (event == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if (!event.getCreatorId().equals(userId)) {
            throw new RuntimeException("无权限删除此活动");
        }

        return removeById(eventId);
    }

    @Override
    public IPage<EventDTO> getEventPage(EventQueryDTO queryDTO) {
        Page<Event> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<Event> eventPage = eventMapper.selectEventPage(page, queryDTO);
        
        return eventPage.convert(this::convertToEventDTO);
    }

    @Override
    public List<EventDTO> searchEvents(String keyword, Integer limit) {
        List<Event> events = eventMapper.searchEvents(keyword, limit);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getNearbyEvents(Double latitude, Double longitude, Double radius, Integer limit) {
        List<Event> events = eventMapper.selectNearbyEvents(latitude, longitude, radius, limit);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByCategory(Integer categoryId, Integer limit) {
        List<Event> events = eventMapper.selectEventsByCategory(categoryId, limit);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getFeaturedEvents(Integer limit) {
        List<Event> events = eventMapper.selectFeaturedEvents(limit);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getPopularEvents(Integer limit) {
        List<Event> events = eventMapper.selectPopularEvents(limit);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean signupEvent(Long userId, EventSignupDTO signupDTO) {
        Event event = getById(signupDTO.getEventId());
        if (event == null) {
            throw new RuntimeException("活动不存在");
        }

        if (event.getStatus() != 1) {
            throw new RuntimeException("活动状态不允许报名");
        }

        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new RuntimeException("活动已满员");
        }

        // 检查是否已报名
        if (isUserSignedUp(userId, signupDTO.getEventId())) {
            throw new RuntimeException("您已报名此活动");
        }

        // 创建报名记录
        EventSignup signup = new EventSignup();
        signup.setEventId(signupDTO.getEventId());
        signup.setUserId(userId);
        signup.setSignStatus(0); // 已报名
        signup.setSignupTime(LocalDateTime.now());
        signup.setRemark(signupDTO.getRemark());
        
        // 这里应该保存到数据库
        // eventSignupMapper.insert(signup);
        
        // 更新活动报名人数
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        updateById(event);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelSignup(Long userId, Long eventId) {
        // 这里应该查询并删除报名记录
        // EventSignup signup = eventSignupMapper.selectByUserIdAndEventId(userId, eventId);
        // if (signup == null) {
        //     throw new RuntimeException("您未报名此活动");
        // }
        // eventSignupMapper.deleteById(signup.getId());
        
        // 更新活动报名人数
        Event event = getById(eventId);
        if (event != null) {
            event.setCurrentParticipants(Math.max(0, event.getCurrentParticipants() - 1));
            updateById(event);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean checkinEvent(Long userId, Long eventId, String location, Double latitude, Double longitude) {
        // 这里应该查询报名记录并更新签到状态
        // EventSignup signup = eventSignupMapper.selectByUserIdAndEventId(userId, eventId);
        // if (signup == null) {
        //     throw new RuntimeException("您未报名此活动");
        // }
        // if (signup.getSignStatus() == 1) {
        //     throw new RuntimeException("您已签到此活动");
        // }
        
        // signup.setSignStatus(1);
        // signup.setCheckinTime(LocalDateTime.now());
        // signup.setCheckinLocation(location);
        // signup.setCheckinLatitude(latitude);
        // signup.setCheckinLongitude(longitude);
        // eventSignupMapper.updateById(signup);

        return true;
    }

    @Override
    public List<EventDTO> getMyEvents(Long userId, String type) {
        List<Event> events;
        if ("created".equals(type)) {
            events = eventMapper.selectEventsByCreator(userId);
        } else {
            events = eventMapper.selectEventsByParticipant(userId);
        }
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getMySignupEvents(Long userId) {
        List<Event> events = eventMapper.selectEventsByParticipant(userId);
        return events.stream().map(this::convertToEventDTO).collect(Collectors.toList());
    }

    @Override
    public boolean likeEvent(Long userId, Long eventId) {
        // 这里应该实现点赞逻辑
        Event event = getById(eventId);
        if (event != null) {
            event.setLikeCount(event.getLikeCount() + 1);
            updateById(event);
        }
        return true;
    }

    @Override
    public boolean unlikeEvent(Long userId, Long eventId) {
        // 这里应该实现取消点赞逻辑
        Event event = getById(eventId);
        if (event != null) {
            event.setLikeCount(Math.max(0, event.getLikeCount() - 1));
            updateById(event);
        }
        return true;
    }

    @Override
    public List<EventCategory> getEventCategories() {
        // 这里应该查询分类列表
        return new ArrayList<>();
    }

    @Override
    public List<EventSignup> getEventSignups(Long eventId) {
        // 这里应该查询报名列表
        return new ArrayList<>();
    }

    @Override
    public boolean isUserSignedUp(Long userId, Long eventId) {
        // 这里应该查询用户是否已报名
        return false;
    }

    @Override
    public boolean isUserLiked(Long userId, Long eventId) {
        // 这里应该查询用户是否已点赞
        return false;
    }

    @Override
    public boolean incrementViewCount(Long eventId) {
        Event event = getById(eventId);
        if (event != null) {
            event.setViewCount(event.getViewCount() + 1);
            updateById(event);
        }
        return true;
    }

    @Override
    public boolean updateEventStatus(Long eventId, Integer status, String reason) {
        Event event = getById(eventId);
        if (event != null) {
            event.setStatus(status);
            event.setAuditReason(reason);
            updateById(event);
        }
        return true;
    }

    /**
     * 转换为EventDTO
     */
    private EventDTO convertToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(event, eventDTO);
        
        // 处理标签
        if (StringUtils.hasText(event.getTags())) {
            eventDTO.setTags(Arrays.asList(event.getTags().split(",")));
        }
        
        // 设置状态描述
        eventDTO.setStatusText(getStatusText(event.getStatus()));
        
        // 设置分类名称
        // 这里应该查询分类名称
        eventDTO.setCategoryName("未分类");
        
        // 设置发起人信息
        // 这里应该查询发起人信息
        eventDTO.setCreatorNickname("用户");
        eventDTO.setCreatorAvatar("");
        
        return eventDTO;
    }

    /**
     * 获取状态描述
     */
    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待审核";
            case 1: return "进行中";
            case 2: return "已满";
            case 3: return "已结束";
            case 4: return "已取消";
            case 5: return "已驳回";
            default: return "未知";
        }
    }
}
