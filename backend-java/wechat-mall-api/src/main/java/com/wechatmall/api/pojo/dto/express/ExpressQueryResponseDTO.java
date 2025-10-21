package com.wechatmall.api.pojo.dto.express;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 快递100查询响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExpressQueryResponseDTO", description = "快递查询结果")
public class ExpressQueryResponseDTO {
    @Schema(description = "快递公司编码")
    private String com;

    @Schema(description = "快递单号")
    private String nu;

    @Schema(description = "当前状态编码")
    private String state;

    @Schema(description = "是否签收")
    private boolean ischeck;

    @Schema(description = "轨迹列表")
    private List<ExpressQueryNodeDTO> data;
}
