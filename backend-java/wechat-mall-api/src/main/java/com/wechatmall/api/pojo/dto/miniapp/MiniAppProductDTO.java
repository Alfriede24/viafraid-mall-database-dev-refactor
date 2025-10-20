package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 小程序端用于展示的商品卡片数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppProductDTO", description = "小程序端商品概要信息")
public class MiniAppProductDTO {
    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "简要描述")
    private String description;

    @Schema(description = "现价")
    private BigDecimal price;

    @Schema(description = "划线价")
    private BigDecimal originalPrice;

    @Schema(description = "会员价")
    private BigDecimal memberPrice;

    @Schema(description = "最低售价")
    private BigDecimal minPrice;

    @Schema(description = "封面图")
    private String picUrl;

    @Schema(description = "主图")
    private String mainImageUrl;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "销量")
    private Integer salesCount;

    @Schema(description = "所属大类")
    private String mainCategory;

    @Schema(description = "二级分类/标签")
    private String categoryId;

    @Schema(description = "系列ID")
    private String seriesId;

    @Schema(description = "品牌ID")
    private String brandId;

    @Schema(description = "附加标签列表")
    private List<String> tags;
}
