package com.wechatmall.api.security.handler;


import com.alibaba.fastjson2.JSON;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 无权限访问类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/24 00:54
 */
public class RestFulAccessDenieHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSONString(ApiResultResponse.error("无权限", Response.SC_FORBIDDEN)));
        response.getWriter().flush();
    }
}
