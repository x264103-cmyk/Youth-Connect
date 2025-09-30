package com.youthconnect.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户DTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 性别(0未知 1男 2女)
     */
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 所在城市
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
     * 状态(1正常 0封禁 2待激活)
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 兴趣标签列表
     */
    private List<String> interests;

    /**
     * 用户等级信息
     */
    private UserLevelDTO userLevel;

    /**
     * 好友数量
     */
    private Integer friendCount;

    /**
     * 是否已关注当前用户
     */
    private Boolean isFollowed;
}
