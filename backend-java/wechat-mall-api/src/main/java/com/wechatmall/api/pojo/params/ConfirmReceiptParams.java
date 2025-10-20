package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 确认收货请求DTO
 * 用于用户确认收货操作的请求参数封�? * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmReceiptParams {
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Integer orderId;
}
