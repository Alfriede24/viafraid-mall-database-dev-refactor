package com.wechatmall.api.security.util;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wechatmall.api.security.config.JwtConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 令牌生成工具类
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/22 20:24
 */
public class JwtUtil {
    /**
     * token 生成
     * @return String token
     */
    public static String sign(String userInfo, JwtConfig jwtConfig) {
        // 设置过期时间
        Date date = new Date(System.currentTimeMillis()+jwtConfig.getExpiration());
        // 私钥加密算法
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        return JWT.create()
                .withHeader(header)
                .withExpiresAt(date)
                .withSubject("商城系统token")
                .withIssuer("Wechatmall")
                .withIssuedAt(DateUtil.date())
                .withClaim("userInfo",userInfo)
                .sign(algorithm);
     }

    /**
     * 令牌验证并返回内容实体
     * @param token token
     * @param secret 密钥
     * @param clazz 返回实体class
     * @return 返回解析实体
     * @param <T> 范型
     */
     public static <T> T verify(String token,String secret,Class<T> clazz) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        String payload = verify.getClaim("userInfo").asString();
        return JSON.parseObject(payload, clazz);
     }
}
