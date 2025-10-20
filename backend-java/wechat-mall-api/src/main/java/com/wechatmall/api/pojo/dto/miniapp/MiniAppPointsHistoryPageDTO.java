package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 积分明细分页响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppPointsHistoryPageDTO", description = "积分明细分页响应")
public class MiniAppPointsHistoryPageDTO {

    @Schema(description = "当前页码")
    private Integer page;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "是否还有下一页")
    private Boolean hasNext;

    @Schema(description = "记录列表")
    private List<MiniAppPointsHistoryItemDTO> items;
}
