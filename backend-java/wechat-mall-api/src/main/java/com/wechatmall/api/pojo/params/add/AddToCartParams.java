package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加商品到购物车请求数据传输对象
 * 用于接收用户添加商品到购物车时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartParams {
    
    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Integer productId;
    
    /**
     * 商品SKU ID
     */
    private Integer productSkuId;
    
    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;
}
