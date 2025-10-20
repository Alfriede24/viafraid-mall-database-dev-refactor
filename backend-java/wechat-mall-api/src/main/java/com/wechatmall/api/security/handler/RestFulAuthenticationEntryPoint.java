package com.wechatmall.api.security.handler;


import com.alibaba.fastjson2.JSON;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 未登录处理类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/24 01:03
 */
public class RestFulAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSONString(ApiResultResponse.error("未登录", Response.SC_UNAUTHORIZED)));
        response.getWriter().flush();
    }
}
