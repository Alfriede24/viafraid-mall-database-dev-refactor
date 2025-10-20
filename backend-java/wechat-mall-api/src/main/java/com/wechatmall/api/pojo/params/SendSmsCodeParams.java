package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 发送短信验证码请求DTO
 * 用于发送短信验证码的请求参数封装
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsCodeParams {
    
    /**
     * 手机号码
     * 必须是有效的中国大陆手机号格式
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    /**
     * 短信类型
     * 1: 登录验证码, 2: 注册验证码, 3: 找回密码, 4: 绑定手机号, 5: 修改手机号
     */
    private Integer type = 1;
    
    /**
     * 图形验证码（可选）
     * 用于防止恶意刷短信
     */
    private String captcha;
    
    /**
     * 图形验证码标识（可选）
     * 与captcha配合使用
     */
    private String captchaKey;
}