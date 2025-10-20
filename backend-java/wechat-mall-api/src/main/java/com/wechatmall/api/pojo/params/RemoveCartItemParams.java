package com.wechatmall.api.pojo.params;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 移除购物车项请求数据传输对象
 * 用于接收用户移除购物车项时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemParams {
    
    /**
     * 购物车项ID列表
     */
    @NotEmpty(message = "购物车项ID列表不能为空")
    private List<Integer> cartItemIds;
}
