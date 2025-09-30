package com.youthconnect.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户积分等级实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_level")
public class UserLevel implements Serializable {

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
     * 积分
     */
    @TableField("points")
    private Integer points;

    /**
     * 等级
     */
    @TableField("level")
    private Integer level;

    /**
     * 经验值
     */
    @TableField("experience")
    private Integer experience;

    /**
     * 下一级所需积分
     */
    @TableField("next_level_points")
    private Integer nextLevelPoints;

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
