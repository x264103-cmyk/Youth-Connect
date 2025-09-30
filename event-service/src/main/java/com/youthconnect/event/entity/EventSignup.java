package com.youthconnect.event.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动报名实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("event_signup")
public class EventSignup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    @TableField("event_id")
    private Long eventId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 报名状态(0已报名 1已签到 2已取消)
     */
    @TableField("sign_status")
    private Integer signStatus;

    /**
     * 报名时间
     */
    @TableField("signup_time")
    private LocalDateTime signupTime;

    /**
     * 签到时间
     */
    @TableField("checkin_time")
    private LocalDateTime checkinTime;

    /**
     * 签到位置
     */
    @TableField("checkin_location")
    private String checkinLocation;

    /**
     * 签到纬度
     */
    @TableField("checkin_latitude")
    private BigDecimal checkinLatitude;

    /**
     * 签到经度
     */
    @TableField("checkin_longitude")
    private BigDecimal checkinLongitude;

    /**
     * 报名备注
     */
    @TableField("remark")
    private String remark;
}
