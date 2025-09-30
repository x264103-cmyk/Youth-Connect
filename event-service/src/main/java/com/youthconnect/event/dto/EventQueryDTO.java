package com.youthconnect.event.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动查询DTO
 */
@Data
public class EventQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 城市筛选
     */
    private String city;

    /**
     * 最小价格
     */
    private BigDecimal minPrice;

    /**
     * 最大价格
     */
    private BigDecimal maxPrice;

    /**
     * 开始时间筛选
     */
    private LocalDateTime startTime;

    /**
     * 结束时间筛选
     */
    private LocalDateTime endTime;

    /**
     * 中心纬度(用于附近活动搜索)
     */
    private BigDecimal latitude;

    /**
     * 中心经度(用于附近活动搜索)
     */
    private BigDecimal longitude;

    /**
     * 搜索半径(公里)
     */
    private Double radius;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 是否精选
     */
    private Integer isFeatured;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向(asc/desc)
     */
    private String sortOrder;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
