package com.wechatmall.api.pojo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 统一用户信息
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/24 01:54
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "用户统一信息",description = "返回用户信息统一实体类")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    @JsonIgnore
    private String password;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "头像地址")
    private String avatar;
}
