package com.youthconnect.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户兴趣标签实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_interest")
public class UserInterest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 兴趣标签
     */
    @TableField("tag")
    private String tag;

    /**
     * 兴趣权重
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
