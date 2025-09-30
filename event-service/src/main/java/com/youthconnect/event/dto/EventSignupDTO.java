package com.youthconnect.event.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 活动报名DTO
 */
@Data
public class EventSignupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    private Long eventId;

    /**
     * 报名备注
     */
    private String remark;
}
