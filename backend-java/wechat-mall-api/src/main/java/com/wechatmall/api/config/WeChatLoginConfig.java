package com.wechatmall.api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 微信登录配置类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 21:06
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatLoginConfig {
    private String appId;
    private String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
