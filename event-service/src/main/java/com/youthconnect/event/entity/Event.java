package com.youthconnect.event.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动发起人ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 活动分类ID
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 活动标题
     */
    @TableField("title")
    private String title;

    /**
     * 活动描述
     */
    @TableField("description")
    private String description;

    /**
     * 封面图片
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 活动地点
     */
    @TableField("location")
    private String location;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 报名截止时间
     */
    @TableField("registration_deadline")
    private LocalDateTime registrationDeadline;

    /**
     * 人数上限
     */
    @TableField("max_participants")
    private Integer maxParticipants;

    /**
     * 当前报名人数
     */
    @TableField("current_participants")
    private Integer currentParticipants;

    /**
     * 活动费用
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系微信
     */
    @TableField("contact_wechat")
    private String contactWechat;

    /**
     * 活动标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 活动状态(0待审核 1进行中 2已满 3已结束 4已取消 5驳回)
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核拒绝原因
     */
    @TableField("audit_reason")
    private String auditReason;

    /**
     * 浏览数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 是否精选(0普通 1精选)
     */
    @TableField("is_featured")
    private Integer isFeatured;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
