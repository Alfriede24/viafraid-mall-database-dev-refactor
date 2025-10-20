package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 支付订单请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderParams {
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Integer orderId;
    
    /**
     * 支付方式
     */
    @NotBlank(message = "支付方式不能为空")
    @Size(max = 50, message = "支付方式长度不能超过50个字符")
    private String payMethod;
}
