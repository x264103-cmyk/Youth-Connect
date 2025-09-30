package com.youthconnect.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 好友关系实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("friendship")
public class Friendship implements Serializable {

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
     * 好友ID
     */
    @TableField("friend_id")
    private Long friendId;

    /**
     * 状态(0待确认 1已同意 2拒绝 3已删除)
     */
    @TableField("status")
    private Integer status;

    /**
     * 备注名
     */
    @TableField("remark")
    private String remark;

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
