package com.wechatmall.api.pojo.params.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 更新购物车项请求数据传输对象
 * 用于接收用户更新购物车项时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemParams {
    
    /**
     * 购物车项ID
     */
    @NotNull(message = "购物车项ID不能为空")
    private Integer id;
    
    /**
     * 购买数量
     */
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;
    
    /**
     * 是否选中
     */
    private Boolean isSelected;
}
