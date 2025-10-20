package com.wechatmall.api.security.filter;


import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.security.config.JwtConfig;
import com.wechatmall.api.security.entity.LoginUser;
import com.wechatmall.api.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: Jwt认证过滤器
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/24 00:06
 */
@Component
public class JwtAuthenticaitonTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtConfig  jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerToken = request.getHeader(jwtConfig.getHeader());
        if(StrUtil.isNotBlank(headerToken)){
            String token = headerToken.replace(jwtConfig.getPrefix(), "").trim();
            try {
                LoginUser user = JwtUtil.verify(token,jwtConfig.getSecret(),LoginUser.class);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw  new RuntimeException(e);
            }
        }
        filterChain.doFilter(request,response);
    }
}
