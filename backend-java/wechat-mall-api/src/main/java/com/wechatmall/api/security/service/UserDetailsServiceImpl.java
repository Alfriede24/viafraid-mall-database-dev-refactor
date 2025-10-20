package com.wechatmall.api.security.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wechatmall.api.dao.RootMapper;
import com.wechatmall.api.pojo.dto.UserInfo;
import com.wechatmall.api.pojo.entity.Root;
import com.wechatmall.api.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 用户信息实现类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 20:45
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private RootMapper rootMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Root> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Root::getUsername,username);
        Root user = rootMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException(username);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(user.getPassword());
        return new LoginUser(userInfo);
    }

    public LoginUser getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (LoginUser) authentication.getPrincipal();
    }
}
