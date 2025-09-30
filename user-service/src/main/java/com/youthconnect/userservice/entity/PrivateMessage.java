package com.youthconnect.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 私信实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("private_message")
public class PrivateMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者ID
     */
    @TableField("sender_id")
    private Long senderId;

    /**
     * 接收者ID
     */
    @TableField("receiver_id")
    private Long receiverId;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型(0文字 1图片 2语音 3文件)
     */
    @TableField("type")
    private Integer type;

    /**
     * 文件URL
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 是否已读(0未读 1已读)
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
