package com.youthconnect.userservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户查询DTO
 */
@Data
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词搜索(昵称、手机号、邮箱)
     */
    private String keyword;

    /**
     * 性别筛选
     */
    private Integer gender;

    /**
     * 城市筛选
     */
    private String location;

    /**
     * 兴趣标签筛选
     */
    private String interest;

    /**
     * 最小年龄
     */
    private Integer minAge;

    /**
     * 最大年龄
     */
    private Integer maxAge;

    /**
     * 最小等级
     */
    private Integer minLevel;

    /**
     * 最大等级
     */
    private Integer maxLevel;

    /**
     * 中心纬度(用于附近的人搜索)
     */
    private BigDecimal latitude;

    /**
     * 中心经度(用于附近的人搜索)
     */
    private BigDecimal longitude;

    /**
     * 搜索半径(公里)
     */
    private Double radius;

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
