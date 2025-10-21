package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppCouponDTO", description = "小程序端优惠券")
public class MiniAppCouponDTO {

    @Schema(description = "优惠券ID")
    private Long id;

    @Schema(description = "优惠券名称")
    private String name;

    @Schema(description = "使用说明")
    private String description;

    @Schema(description = "面值")
    private Integer value;

    @Schema(description = "有效期描述")
    private String validPeriod;

    @Schema(description = "状态 unused/used/expired")
    private String status;

    @Schema(description = "使用门槛说明")
    private String threshold;

    @Schema(description = "优惠券类型，如满减、无门槛")
    private String type;
}
