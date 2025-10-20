package com.wechatmall.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.wechatmall.api.pojo.dto.RootDto;
import com.wechatmall.api.pojo.entity.Root;
import com.wechatmall.api.dao.RootMapper;
import com.wechatmall.api.security.config.JwtConfig;
import com.wechatmall.api.security.entity.LoginUser;
import com.wechatmall.api.security.util.JwtUtil;
import com.wechatmall.api.service.IRootService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
@Service
@AllArgsConstructor
public class RootServiceImpl extends ServiceImpl<RootMapper, Root> implements IRootService {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;


    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(auth);
        if(authentication == null){
            return null;
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return JwtUtil.sign(JSON.toJSONString(loginUser),jwtConfig);
    }

    @Override
    public String register(RootDto userDto) {
        Root root = new Root();
        BeanUtil.copyProperties(userDto,root);
        BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
        root.setPassword(crypt.encode(userDto.getPassword()));
        boolean result = this.save(root);
        if(result){
            return "注册成功";
        }
        return "注册失败";
    }
}
