package com.youthconnect.userservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户等级DTO
 */
@Data
public class UserLevelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 经验值
     */
    private Integer experience;

    /**
     * 下一级所需积分
     */
    private Integer nextLevelPoints;

    /**
     * 等级名称
     */
    private String levelName;
}
