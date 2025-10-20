package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 创建用户地址请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAddressParams {
    
    /**
     * 收件人姓名
     */
    @NotBlank(message = "收件人姓名不能为空")
    @Size(max = 50, message = "收件人姓名长度不能超过50个字符")
    private String receiverName;
    
    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    @Size(max = 50, message = "省份长度不能超过50个字符")
    private String province;
    
    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;
    
    /**
     * 区县
     */
    @NotBlank(message = "区县不能为空")
    @Size(max = 50, message = "区县长度不能超过50个字符")
    private String district;
    
    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String detailAddress;
    
    /**
     * 邮政编码
     */
    @Size(max = 10, message = "邮政编码长度不能超过10个字符")
    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postalCode;
    
    /**
     * 是否默认地址
     */
    private Boolean isDefault = false;
}
