package com.wechatmall.api.pojo.dto.express;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 快递100查询节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExpressQueryNodeDTO", description = "快递查询轨迹节点")
public class ExpressQueryNodeDTO {
    @Schema(description = "节点时间")
    private String time;

    @Schema(description = "节点描述")
    private String context;

    @Schema(description = "节点状态")
    private String status;

    @Schema(description = "节点所在地")
    private String location;
}
