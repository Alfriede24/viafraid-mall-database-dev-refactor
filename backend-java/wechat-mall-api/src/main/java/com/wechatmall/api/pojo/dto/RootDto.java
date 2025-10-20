package com.wechatmall.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Description: 商家注册DTO <br>
 * WebSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * Copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * Program Name: MechrantDto <br>
 * Date: 2025-09-24  下午 10:23 <br>
 *
 * @author PengCai Zhu Zhupc1024@163.com
 * @version 1.0
 */
@Data
public class RootDto {
    
    @Schema(description = "用户名",name = "username")
    private String username;
    @Schema(description = "密码",name = "password")
    private String password;
    @Schema(description = "权限",name = "auth")
    private String auth;
//    @Schema(description = "昵称",name = "nickname")
//    private String nickname;
//    @Schema(description = "手机号",name = "phone")
//    private String phone;
//    @Schema(description = "邮箱",name = "email")
//    private String email;
    // @Schema(name = "头像地址")
    // private String avatar;
}
