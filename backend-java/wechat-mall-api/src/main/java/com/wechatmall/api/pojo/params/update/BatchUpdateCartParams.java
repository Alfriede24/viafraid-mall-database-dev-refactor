package com.wechatmall.api.pojo.params.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量更新购物车项请求数据传输对象
 * 用于接收用户批量更新购物车项时的请求参数
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchUpdateCartParams {
    
    /**
     * 更新项列表
     */
    @NotEmpty(message = "更新项列表不能为空")
    @Valid
    private List<UpdateCartItemParams> items;
}
