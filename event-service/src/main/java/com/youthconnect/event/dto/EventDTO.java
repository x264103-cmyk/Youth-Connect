package com.youthconnect.event.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动DTO
 */
@Data
public class EventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 活动发起人ID
     */
    private Long creatorId;

    /**
     * 发起人昵称
     */
    private String creatorNickname;

    /**
     * 发起人头像
     */
    private String creatorAvatar;

    /**
     * 活动分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 报名截止时间
     */
    private LocalDateTime registrationDeadline;

    /**
     * 人数上限
     */
    private Integer maxParticipants;

    /**
     * 当前报名人数
     */
    private Integer currentParticipants;

    /**
     * 活动费用
     */
    private BigDecimal price;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系微信
     */
    private String contactWechat;

    /**
     * 活动标签
     */
    private List<String> tags;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusText;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否精选
     */
    private Integer isFeatured;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已报名
     */
    private Boolean isSignedUp;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 距离(公里)
     */
    private Double distance;
}
