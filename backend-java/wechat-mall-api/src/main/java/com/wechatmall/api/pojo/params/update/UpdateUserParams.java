package com.wechatmall.api.pojo.params.update;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户信息请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserParams {
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickName;
    
    /**
     * 头像URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatarUrl;
    
    /**
     * 性别
     * 0: 未知, 1: 男性, 2: 女性
     */
    private Integer gender;
    
    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 邮箱
     */
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Email(message = "邮箱格式不正确")
    private String email;
}
