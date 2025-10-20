package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建商品SKU请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductSkuParams {
    
    /**
     * SKU编码
     */
    @NotBlank(message = "SKU编码不能为空")
    @Size(max = 100, message = "SKU编码长度不能超过100个字符")
    private String skuCode;
    
    /**
     * SKU名称
     */
    @Size(max = 200, message = "SKU名称长度不能超过200个字符")
    private String skuName;
    
    /**
     * 规格参数
     */
    private Map<String, String> specifications = new HashMap<>();
    
    /**
     * SKU价格
     */
    @NotNull(message = "SKU价格不能为空")
    @DecimalMin(value = "0.01", message = "SKU价格必须在0.01-999999.99之间")
    @DecimalMax(value = "999999.99", message = "SKU价格必须在0.01-999999.99之间")
    private BigDecimal price;
    
    /**
     * 原价
     */
    @DecimalMin(value = "0.01", message = "原价必须在0.01-999999.99之间")
    @DecimalMax(value = "999999.99", message = "原价必须在0.01-999999.99之间")
    private BigDecimal originalPrice;
    
    /**
     * SKU库存
     */
    @NotNull(message = "SKU库存不能为空")
    @Min(value = 0, message = "SKU库存不能为负数")
    private Integer stock;
    
    /**
     * SKU图片URL
     */
    @Size(max = 500, message = "SKU图片URL长度不能超过500个字符")
    private String imageUrl;
}
