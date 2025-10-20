package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收藏创建入参
 */
@Data
@Schema(name = "MiniAppFavoriteCreateRequest", description = "收藏商品请求")
public class MiniAppFavoriteCreateRequest {

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "展示图片")
    private String image;

    @Schema(description = "价格")
    private String price;
}
