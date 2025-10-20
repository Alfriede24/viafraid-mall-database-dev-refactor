package com.wechatmall.api.controller.miniapp;

import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppOrderAddressDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppOrderDetailDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppOrderItemDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppOrderLogisticsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小程序订单模拟接口
 */
@RestController
@RequestMapping("/miniapp/orders")
@Validated
@Tag(name = "小程序订单", description = "为小程序端提供订单详情与状态更新的模拟接口")
public class MiniAppOrderController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Map<Long, MiniAppOrderDetailDTO> ORDER_STORE = new ConcurrentHashMap<>();

    static {
        MiniAppOrderDetailDTO order = MiniAppOrderDetailDTO.builder()
            .id(20240518001L)
            .orderNo("WX20240518001")
            .status("shipped")
            .statusText("待收货")
            .createTime("2025-05-18 14:30:00")
            .payTime("2025-05-18 14:32:15")
            .totalAmount(new BigDecimal("299"))
            .freight(new BigDecimal("10"))
            .discount(new BigDecimal("20"))
            .actualAmount(new BigDecimal("289"))
            .payMethod("微信支付")
            .address(MiniAppOrderAddressDTO.builder()
                .name("张三")
                .phone("138****8888")
                .region("广东省 深圳市 南山区")
                .detail("科技园南区深南大道10000号")
                .build())
            .items(List.of(
                MiniAppOrderItemDTO.builder()
                    .id(10001L)
                    .name("Air Jordan 1 High OG")
                    .image("https://cdn.wechatmall.dev/products/aj1-main.png")
                    .price(new BigDecimal("149"))
                    .quantity(2)
                    .specs("42码 湖人配色")
                    .build()
            ))
            .logistics(MiniAppOrderLogisticsDTO.builder()
                .company("顺丰速运")
                .companyCode("SF")
                .trackingNo("SF1234567890")
                .status("shipped")
                .updateTime("2025-05-19 10:30:00")
                .build())
            .build();
        ORDER_STORE.put(order.getId(), order);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "订单详情", description = "根据订单ID返回小程序端订单详情")
    public ApiResultResponse<MiniAppOrderDetailDTO> getOrderDetail(@PathVariable Long orderId) {
        MiniAppOrderDetailDTO detail = ORDER_STORE.get(orderId);
        if (detail == null) {
            return ApiResultResponse.error("订单不存在", 404);
        }
        return ApiResultResponse.ok("查询成功", detail);
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单", description = "模拟取消订单操作")
    public ApiResultResponse<MiniAppOrderDetailDTO> cancelOrder(@PathVariable Long orderId) {
        MiniAppOrderDetailDTO detail = ORDER_STORE.get(orderId);
        if (detail == null) {
            return ApiResultResponse.error("订单不存在", 404);
        }
        if (StrUtil.equalsAny(detail.getStatus(), "cancelled", "completed")) {
            return ApiResultResponse.ok("状态未变化", detail);
        }
        detail.setStatus("cancelled");
        detail.setStatusText("已取消");
        detail.setPayMethod(null);
        detail.setLogistics(null);
        return ApiResultResponse.ok("订单已取消", detail);
    }

    @PostMapping("/{orderId}/pay")
    @Operation(summary = "支付订单", description = "模拟支付成功后的状态更新")
    public ApiResultResponse<MiniAppOrderDetailDTO> payOrder(@PathVariable Long orderId) {
        MiniAppOrderDetailDTO detail = ORDER_STORE.get(orderId);
        if (detail == null) {
            return ApiResultResponse.error("订单不存在", 404);
        }
        detail.setStatus("paid");
        detail.setStatusText("待发货");
        detail.setPayMethod("微信支付");
        detail.setPayTime(DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        return ApiResultResponse.ok("支付成功", detail);
    }

    @PostMapping("/{orderId}/confirm")
    @Operation(summary = "确认收货", description = "模拟确认收货操作")
    public ApiResultResponse<MiniAppOrderDetailDTO> confirmOrder(@PathVariable Long orderId) {
        MiniAppOrderDetailDTO detail = ORDER_STORE.get(orderId);
        if (detail == null) {
            return ApiResultResponse.error("订单不存在", 404);
        }
        detail.setStatus("completed");
        detail.setStatusText("已完成");
        detail.setLogistics(detail.getLogistics() == null ? null : detail.getLogistics());
        return ApiResultResponse.ok("确认收货成功", detail);
    }
}
