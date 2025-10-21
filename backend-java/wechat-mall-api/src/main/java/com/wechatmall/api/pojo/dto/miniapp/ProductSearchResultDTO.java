package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小程序商品搜索返回结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductSearchResultDTO", description = "小程序端搜索结果")
public class ProductSearchResultDTO {
    @Schema(description = "商品数据列表")
    private List<MiniAppProductDTO> items;

    @Schema(description = "总条数")
    private long total;

    @Schema(description = "是否还有更多")
    private boolean hasMore;

    @Schema(description = "当前页码")
    private int page;

    @Schema(description = "分页大小")
    private int pageSize;
}
