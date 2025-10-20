package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单物流信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppOrderLogisticsDTO", description = "订单物流信息")
public class MiniAppOrderLogisticsDTO {

    @Schema(description = "物流公司名称")
    private String company;

    @Schema(description = "物流公司编码")
    private String companyCode;

    @Schema(description = "运单号")
    private String trackingNo;

    @Schema(description = "当前状态")
    private String status;

    @Schema(description = "最近更新时间")
    private String updateTime;
}
