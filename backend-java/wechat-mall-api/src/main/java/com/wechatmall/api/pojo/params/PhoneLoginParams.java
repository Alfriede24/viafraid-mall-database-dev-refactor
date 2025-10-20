package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 手机号登录请求DTO
 * 用于手机号验证码登录的请求参数封装
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneLoginParams {
    
    /**
     * 手机号码
     * 必须是有效的中国大陆手机号格式
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    /**
     * 验证码
     * 6位数字验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须是6位数字")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    private String code;
    
    /**
     * 微信OpenID（可选）
     * 用于绑定微信账号
     */
    private String openId;
    
    /**
     * 微信UnionID（可选）
     * 用于跨应用用户识别
     */
    private String unionId;
    
    /**
     * 用户昵称（可选）
     * 从微信获取的用户昵称
     */
    private String nickName;
    
    /**
     * 用户头像URL（可选）
     * 从微信获取的用户头像
     */
    private String avatarUrl;
    
    /**
     * 用户性别（可选）
     * 0: 未知, 1: 男性, 2: 女性
     */
    private Integer gender;
}