package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单收货地址
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppOrderAddressDTO", description = "订单收货地址信息")
public class MiniAppOrderAddressDTO {

    @Schema(description = "收货人姓名")
    private String name;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "省市区")
    private String region;

    @Schema(description = "详细地址")
    private String detail;
}
