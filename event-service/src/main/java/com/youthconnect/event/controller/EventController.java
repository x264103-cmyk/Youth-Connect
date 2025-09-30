package com.youthconnect.event.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youthconnect.event.common.Result;
import com.youthconnect.event.dto.*;
import com.youthconnect.event.entity.Event;
import com.youthconnect.event.entity.EventCategory;
import com.youthconnect.event.entity.EventSignup;
import com.youthconnect.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * 创建活动
     */
    @PostMapping
    public Result<EventDTO> createEvent(@Valid @RequestBody EventCreateDTO createDTO) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            EventDTO eventDTO = eventService.createEvent(userId, createDTO);
            return Result.success(eventDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{id}")
    public Result<EventDTO> getEventById(@PathVariable Long id) {
        try {
            EventDTO eventDTO = eventService.getEventById(id);
            if (eventDTO == null) {
                return Result.error("活动不存在");
            }
            return Result.success(eventDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新活动
     */
    @PutMapping("/{id}")
    public Result<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventCreateDTO createDTO) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            EventDTO eventDTO = eventService.updateEvent(userId, id, createDTO);
            return Result.success(eventDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteEvent(@PathVariable Long id) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = eventService.deleteEvent(userId, id);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询活动列表
     */
    @PostMapping("/page")
    public Result<IPage<EventDTO>> getEventPage(@RequestBody EventQueryDTO queryDTO) {
        IPage<EventDTO> eventPage = eventService.getEventPage(queryDTO);
        return Result.success(eventPage);
    }

    /**
     * 搜索活动
     */
    @GetMapping("/search")
    public Result<List<EventDTO>> searchEvents(@RequestParam String keyword,
                                             @RequestParam(defaultValue = "20") Integer limit) {
        List<EventDTO> events = eventService.searchEvents(keyword, limit);
        return Result.success(events);
    }

    /**
     * 查询附近活动
     */
    @GetMapping("/nearby")
    public Result<List<EventDTO>> getNearbyEvents(@RequestParam Double latitude,
                                                 @RequestParam Double longitude,
                                                 @RequestParam(defaultValue = "10.0") Double radius,
                                                 @RequestParam(defaultValue = "20") Integer limit) {
        List<EventDTO> events = eventService.getNearbyEvents(latitude, longitude, radius, limit);
        return Result.success(events);
    }

    /**
     * 根据分类查询活动
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<EventDTO>> getEventsByCategory(@PathVariable Integer categoryId,
                                                     @RequestParam(defaultValue = "20") Integer limit) {
        List<EventDTO> events = eventService.getEventsByCategory(categoryId, limit);
        return Result.success(events);
    }

    /**
     * 获取精选活动
     */
    @GetMapping("/featured")
    public Result<List<EventDTO>> getFeaturedEvents(@RequestParam(defaultValue = "10") Integer limit) {
        List<EventDTO> events = eventService.getFeaturedEvents(limit);
        return Result.success(events);
    }

    /**
     * 获取热门活动
     */
    @GetMapping("/popular")
    public Result<List<EventDTO>> getPopularEvents(@RequestParam(defaultValue = "10") Integer limit) {
        List<EventDTO> events = eventService.getPopularEvents(limit);
        return Result.success(events);
    }

    /**
     * 活动报名
     */
    @PostMapping("/signup")
    public Result<Boolean> signupEvent(@Valid @RequestBody EventSignupDTO signupDTO) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            System.out.println(signupDTO.getEventId());
            System.out.println(signupDTO.getRemark());
            boolean success = eventService.signupEvent(userId, signupDTO);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消报名
     */
    @DeleteMapping("/signup/{eventId}")
    public Result<Boolean> cancelSignup(@PathVariable Long eventId) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = eventService.cancelSignup(userId, eventId);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 活动签到
     */
    @PostMapping("/checkin/{eventId}")
    public Result<Boolean> checkinEvent(@PathVariable Long eventId,
                                       @RequestParam(required = false) String location,
                                       @RequestParam(required = false) Double latitude,
                                       @RequestParam(required = false) Double longitude) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = eventService.checkinEvent(userId, eventId, location, latitude, longitude);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的活动列表
     */
    @GetMapping("/my")
    public Result<List<EventDTO>> getMyEvents(@RequestParam(defaultValue = "created") String type) {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        List<EventDTO> events = eventService.getMyEvents(userId, type);
        return Result.success(events);
    }

    /**
     * 获取我报名的活动列表
     */
    @GetMapping("/my/signup")
    public Result<List<EventDTO>> getMySignupEvents() {
        // 这里应该从JWT token中获取当前用户ID
        Long userId = 1L; // 临时硬编码
        List<EventDTO> events = eventService.getMySignupEvents(userId);
        return Result.success(events);
    }

    /**
     * 点赞活动
     */
    @PostMapping("/{id}/like")
    public Result<Boolean> likeEvent(@PathVariable Long id) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = eventService.likeEvent(userId, id);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{id}/like")
    public Result<Boolean> unlikeEvent(@PathVariable Long id) {
        try {
            // 这里应该从JWT token中获取当前用户ID
            Long userId = 1L; // 临时硬编码
            boolean success = eventService.unlikeEvent(userId, id);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取活动分类列表
     */
    @GetMapping("/categories")
    public Result<List<EventCategory>> getEventCategories() {
        List<EventCategory> categories = eventService.getEventCategories();
        return Result.success(categories);
    }

    /**
     * 获取活动报名列表
     */
    @GetMapping("/{id}/signups")
    public Result<List<EventSignup>> getEventSignups(@PathVariable Long id) {
        List<EventSignup> signups = eventService.getEventSignups(id);
        return Result.success(signups);
    }
}
