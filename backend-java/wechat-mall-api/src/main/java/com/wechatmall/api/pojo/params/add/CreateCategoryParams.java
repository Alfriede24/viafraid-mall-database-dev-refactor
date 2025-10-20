package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 创建分类请求DTO
 * 用于创建新分类的请求参数封装
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryParams {
    
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;
    
    /**
     * 分类描述
     */
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
    
    /**
     * 分类图片URL
     */
    @Size(max = 500, message = "分类图片URL长度不能超过500个字符")
    private String imageUrl;
    
    /**
     * 父分类ID
     */
    private Integer parentId;
    
    /**
     * 排序顺序
     */
    @Min(value = 0, message = "排序顺序不能为负数")
    private Integer sortOrder = 0;
}