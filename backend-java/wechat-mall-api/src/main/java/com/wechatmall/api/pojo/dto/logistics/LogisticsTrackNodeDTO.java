package com.wechatmall.api.pojo.dto.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流轨迹节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LogisticsTrackNodeDTO", description = "物流轨迹节点")
public class LogisticsTrackNodeDTO {
    @Schema(description = "节点时间")
    private String time;

    @Schema(description = "节点描述")
    private String description;

    @Schema(description = "节点所在地")
    private String location;

    @Schema(description = "节点状态")
    private String status;
}
