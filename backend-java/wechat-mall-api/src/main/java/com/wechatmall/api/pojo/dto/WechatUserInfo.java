package com.wechatmall.api.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 微信登录返回用户信息实体
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 21:20
 */
@Data
public class WechatUserInfo {
    
    @Schema(description = "微信用户唯一标识")
    private String openId;
    @Schema(description = "微信用户昵称")
    private String nickName;
    @Schema(description = "微信用户头像")
    private String unionid;
//    private String city;
//    private String country;
//    private String province;
    @Schema(description = "微信认证返回错误消息")
    private String errmsg;
    @Schema(description = "微信认证返回错误码")
    private Integer errcode;
}
