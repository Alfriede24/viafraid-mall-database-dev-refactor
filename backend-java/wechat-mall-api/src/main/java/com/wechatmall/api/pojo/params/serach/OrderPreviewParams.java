package com.wechatmall.api.pojo.params.serach;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单预览请求DTO
 * 用于订单预览时的请求参数封装
 * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreviewParams {
    
    /**
     * 购物车项ID列表
     * 需要预览的购物车项
     */
    @NotEmpty(message = "购物车项不能为空")
    private List<Integer> cartItemIds;
    
    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    private Integer addressId;
    
    /**
     * 优惠券ID（可选）
     */
    private Integer couponId;
    
    /**
     * 配送方式
     * 1: 普通配送, 2: 快速配送, 3: 预约配送
     */
    private Integer deliveryType = 1;
    
    /**
     * 预约配送时间（可选）
     * 当配送方式为预约配送时必填
     */
    private String appointmentTime;
    
    /**
     * 是否使用积分抵扣
     */
    private Boolean usePoints = false;
    
    /**
     * 使用的积分数量
     */
    private Integer pointsAmount = 0;
    
    /**
     * 备注信息（可选）
     */
    private String remark;
}