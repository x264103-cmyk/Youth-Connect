package com.youthconnect.userservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录账号(手机号或邮箱)
     */
    @NotBlank(message = "登录账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 登录类型(phone/email)
     */
    private String loginType;

    /**
     * 记住我
     */
    private Boolean rememberMe = false;
}
