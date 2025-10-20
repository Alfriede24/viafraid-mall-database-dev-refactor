package com.wechatmall.api.pojo.params.update;

import com.wechatmall.api.pojo.params.add.CreateProductParams;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 更新商品请求DTO
 * 用于更新商品信息的请求参数封装，继承自CreateProductRequest并添加商品ID
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductParams extends CreateProductParams {
    
    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Integer id;
}
