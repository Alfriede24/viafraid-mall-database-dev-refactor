package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分明细记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppPointsHistoryItemDTO", description = "积分明细记录")
public class MiniAppPointsHistoryItemDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "时间")
    private String createdAt;

    @Schema(description = "积分变动数值")
    private Integer points;

    @Schema(description = "类型 earn/consume")
    private String type;
}
