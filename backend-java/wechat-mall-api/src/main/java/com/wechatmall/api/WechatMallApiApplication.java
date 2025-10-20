package com.wechatmall.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wechatmall.api.dao")
public class WechatMallApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMallApiApplication.class, args);
    }

}
