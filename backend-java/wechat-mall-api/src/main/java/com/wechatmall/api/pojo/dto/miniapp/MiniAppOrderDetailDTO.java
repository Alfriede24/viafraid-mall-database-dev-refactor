package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppOrderDetailDTO", description = "小程序端订单详情")
public class MiniAppOrderDetailDTO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态编码")
    private String status;

    @Schema(description = "订单状态文案")
    private String statusText;

    @Schema(description = "下单时间")
    private String createTime;

    @Schema(description = "支付时间")
    private String payTime;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "运费")
    private BigDecimal freight;

    @Schema(description = "优惠金额")
    private BigDecimal discount;

    @Schema(description = "实付金额")
    private BigDecimal actualAmount;

    @Schema(description = "支付方式")
    private String payMethod;

    @Schema(description = "收货地址")
    private MiniAppOrderAddressDTO address;

    @Schema(description = "商品列表")
    private List<MiniAppOrderItemDTO> items;

    @Schema(description = "物流信息")
    private MiniAppOrderLogisticsDTO logistics;
}
