package com.wechatmall.api.pojo.params.update;

import com.wechatmall.api.pojo.params.add.CreateCategoryParams;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 更新分类请求DTO
 * 用于更新分类信息的请求参数封装，继承自CreateCategoryRequest并添加分类ID
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryParams extends CreateCategoryParams {
    
    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Integer id;
}