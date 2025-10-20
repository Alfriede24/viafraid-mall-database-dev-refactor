package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建订单请求数据传输对象
 * 用于接收用户创建订单时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderParams {
    
    /**
     * 购物车项ID列表
     */
    @NotEmpty(message = "购物车项不能为空")
    private List<Integer> cartItemIds = new ArrayList<>();
    
    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    private Integer addressId;
    
    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    /**
     * 优惠券金�?     */
    private BigDecimal couponAmount;
}
