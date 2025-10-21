package com.wechatmall.api.pojo.dto.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 物流查询返回
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LogisticsTrackResponseDTO", description = "物流查询响应")
public class LogisticsTrackResponseDTO {
    @Schema(description = "快递公司")
    private String company;

    @Schema(description = "快递公司编码")
    private String companyCode;

    @Schema(description = "运单号")
    private String trackingNo;

    @Schema(description = "快递100状态码")
    private String state;

    @Schema(description = "当前状态文本")
    private String status;

    @Schema(description = "最近更新时间")
    private String updateTime;

    @Schema(description = "收件人信息")
    private LogisticsRecipientDTO recipient;

    @Schema(description = "轨迹节点")
    private List<LogisticsTrackNodeDTO> tracks;
}
