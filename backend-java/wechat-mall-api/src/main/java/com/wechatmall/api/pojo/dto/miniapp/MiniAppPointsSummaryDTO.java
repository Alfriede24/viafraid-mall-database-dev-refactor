package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分汇总数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppPointsSummaryDTO", description = "积分汇总信息")
public class MiniAppPointsSummaryDTO {

    @Schema(description = "可用积分")
    private Integer availablePoints;

    @Schema(description = "冻结积分")
    private Integer frozenPoints;

    @Schema(description = "累计获得积分")
    private Integer totalEarned;

    @Schema(description = "累计使用积分")
    private Integer totalSpent;
}
