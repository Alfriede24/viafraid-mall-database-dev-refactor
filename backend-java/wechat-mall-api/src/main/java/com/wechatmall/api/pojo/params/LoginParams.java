package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求数据传输对象
 * 用于接收微信小程序登录时的用户信�? * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParams {
    
    /**
     * 微信授权码
     */
    @NotBlank(message = "微信授权码不能为空")
    private String code;
    
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 头像URL
     */
    private String avatarUrl;
    
    /**
     * 用户性别
     * 0: 未知, 1: 男性, 2: 女性
     * */
    private Integer gender;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
}
