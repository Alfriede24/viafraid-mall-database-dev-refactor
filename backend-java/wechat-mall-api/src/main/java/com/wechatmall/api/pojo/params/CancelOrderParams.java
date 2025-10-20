package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 取消订单请求数据传输对象
 * 用于接收用户取消订单时的请求参数，包含订单ID和取消原�? * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderParams {
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Integer orderId;
    
    /**
     * 取消原因
     */
    @Size(max = 200, message = "取消原因长度不能超过200个字符")
    private String reason;
}
