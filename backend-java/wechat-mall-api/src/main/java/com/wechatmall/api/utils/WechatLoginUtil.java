package com.wechatmall.api.utils;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wechatmall.api.config.WeChatLoginConfig;
import com.wechatmall.api.pojo.dto.WechatUserInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 微信登录工具类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 21:10
 */
@Slf4j
public class WechatLoginUtil {

   public static WechatUserInfo getOpenId(String code, WeChatLoginConfig weChatLoginConfig) {
       String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ weChatLoginConfig.getAppId()+"&secret="+weChatLoginConfig.getAppSecret()+"&js_code="+code+"&grant_type=authorization_code";
       HttpRequest httpRequest = HttpRequest.get(url);
       HttpResponse execute = httpRequest.execute();
       int status = execute.getStatus();
       if (status == 200) {
           String body = execute.body();
           WechatUserInfo jsonObject = JSON.parseObject(body, WechatUserInfo.class);
           return jsonObject;
       }else  {
           log.error("登录失败!"+execute.body());
           return null;
       }
   }

}
