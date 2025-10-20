package com.wechatmall.api.security.entity;


import com.wechatmall.api.pojo.dto.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 登录用户
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 20:41
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private UserInfo user;
    public LoginUser(UserInfo user) {
        this.user = user;
    }

    // 权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // 返回密码
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 用户名
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 账号是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 认证过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
