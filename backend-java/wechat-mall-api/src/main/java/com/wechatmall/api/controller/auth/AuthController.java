package com.wechatmall.api.controller.auth;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.RootDto;
import com.wechatmall.api.service.IRootService;
import com.wechatmall.api.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 用户控制器
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/23 22:53
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证", description = "登录认证管理")
public class AuthController {
    @Autowired
    private IRootService rootService;

    @PostMapping("/login")
    @Operation(summary = "商户后台登录")
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<String> login(@Parameter(name = "username" ,description = "用户名") @RequestParam("username") String username,@Parameter(name="password",description = "密码") @RequestParam("password") String password){
        String jwt = rootService.login(username,password);
        if(StrUtil.isNotBlank(jwt)){
            return ApiResultResponse.ok("登录成功",jwt);
        }
        return ApiResultResponse.error("登录失败");
    }
    
    // @PostMapping("/logout")
    // @Operation(summary = "商户后台登出")
    // @ApiOperationSupport(author = "zpc")
    // public String logout(){
    //     return "logout";
    // }
    
    @PostMapping("/wechat/login")
    @Operation(summary = "微信登录")
    @ApiOperationSupport(author = "zpc")
    public String wechatLogin(@Parameter(name = "code" ,description = "微信授权码") @RequestParam("code") String code){
        // TODO: 微信登录
//        userService.wechatLogin(code);
        return "code";
    }
    
    @PostMapping("/root/register")
    @Operation(summary = "商户注册")
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<String> register(@RequestBody RootDto rootDto){
        return ApiResultResponse.ok(rootService.register(rootDto));
    }
}
