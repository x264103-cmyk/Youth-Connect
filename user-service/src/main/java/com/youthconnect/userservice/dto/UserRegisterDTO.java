package com.youthconnect.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 用户注册DTO
 */
@Data
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度必须在6-20位之间")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$", message = "昵称长度必须在2-20位之间")
    private String nickname;

    /**
     * 性别(0未知 1男 2女)
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 所在城市
     */
    private String location;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 兴趣标签
     */
    private List<String> interests;

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;
}
