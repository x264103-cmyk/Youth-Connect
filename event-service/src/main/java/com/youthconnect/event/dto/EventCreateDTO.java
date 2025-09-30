package com.youthconnect.event.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动创建DTO
 */
@Data
public class EventCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动分类ID
     */
    @NotNull(message = "活动分类不能为空")
    private Integer categoryId;

    /**
     * 活动标题
     */
    @NotBlank(message = "活动标题不能为空")
    private String title;

    /**
     * 活动描述
     */
    @NotBlank(message = "活动描述不能为空")
    private String description;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 活动地点
     */
    @NotBlank(message = "活动地点不能为空")
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
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 报名截止时间
     */
    private LocalDateTime registrationDeadline;

    /**
     * 人数上限
     */
    @NotNull(message = "人数上限不能为空")
    private Integer maxParticipants;

    /**
     * 活动费用
     */
    private BigDecimal price = BigDecimal.ZERO;

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
}
