package com.wechatmall.api.pojo.vo;


import com.wechatmall.api.pojo.entity.User;
import com.wechatmall.api.pojo.entity.UserInformation;
import com.wechatmall.api.pojo.entity.UserOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 用户信息返回实体
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/10/19 09:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends User implements Serializable  {
    @Schema(description = "用户信息")
    UserInformation userInfo;
}
