
package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置默认地址请求数据传输对象
 * 用于接收用户设置默认收货地址时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetDefaultAddressParams {
    
    /**
     * 地址ID
     */
    @NotNull(message = "地址ID不能为空")
    private Integer id;
}
