package com.wechatmall.api.pojo.params.add;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建商品请求DTO
 * 用于创建新商品的请求参数封装，包含商品基本信息、价格、库存等
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductParams {
    
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;
    
    /**
     * 商品描述
     */
    @Size(max = 1000, message = "商品描述长度不能超过1000个字符")
    private String description;
    
    /**
     * 商品详情HTML
     */
    @Size(max = 2000, message = "商品详情长度不能超过2000个字符")
    private String detailHtml;
    
    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须在0.01-999999.99之间")
    @DecimalMax(value = "999999.99", message = "商品价格必须在0.01-999999.99之间")
    private BigDecimal price;
    
    /**
     * 原价
     */
    @DecimalMin(value = "0.01", message = "原价必须在0.01-999999.99之间")
    @DecimalMax(value = "999999.99", message = "原价必须在0.01-999999.99之间")
    private BigDecimal originalPrice;
    
    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stock;
    
    /**
     * 单位
     */
    @Size(max = 100, message = "单位长度不能超过100个字符")
    private String unit;
    
    /**
     * 主图URL
     */
    @Size(max = 500, message = "主图URL长度不能超过500个字符")
    private String mainImageUrl;
    
    /**
     * 图片URL列表
     */
    private List<String> imageUrls = new ArrayList<>();
    
    /**
     * 商品分类ID
     */
    @NotNull(message = "商品分类不能为空")
    private Integer categoryId;
    
    /**
     * 是否推荐
     */
    private Boolean isRecommended = false;
    
    /**
     * 是否新品
     */
    private Boolean isNew = false;
    
    /**
     * 是否热销
     */
    private Boolean isHot = false;
    
    /**
     * SKU列表
     */
    private List<CreateProductSkuParams> skus = new ArrayList<>();
}
