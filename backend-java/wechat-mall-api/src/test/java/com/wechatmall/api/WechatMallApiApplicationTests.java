package com.wechatmall.api;

import com.alibaba.fastjson2.JSON;
import com.wechatmall.api.security.config.JwtConfig;
import com.wechatmall.api.security.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WechatMallApiApplicationTests {
    @Autowired
    private JwtConfig jwtConfig;

    @Test
    void contextLoads() {
//        Users userInfo = new Users();
//        userInfo.setNickName("test");
//        String token = JwtUtil.sign(JSON.toJSONString(userInfo), jwtConfig);
//        System.out.println(token);
//        Users parseUser = JwtUtil.verify(token,jwtConfig.getSecret(),Users.class);
//        System.out.println(JSON.toJSONString(parseUser));
    }
}
