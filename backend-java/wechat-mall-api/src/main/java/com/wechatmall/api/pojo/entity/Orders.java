package com.wechatmall.api.pojo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 实体类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:58:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Orders", description = "实体")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;

    @Schema(description = "用户Id", example = "1")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "总价", example = "示例值")
    @TableField("amount_count")
    private String amountCount;

    @Schema(description = "运费", example = "示例值")
    @TableField("shipping")
    private String shipping;

    @Schema(description = "地址Id", example = "1")
    @TableField("address_id")
    private Integer addressId;

    @Schema(description = "订单状态", example = "示例值")
    @TableField("state")
    private String state;

    @Schema(description = "支付状态", example = "示例值")
    @TableField("payment_state")
    private String paymentState;

    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "快递对应Id", example = "1")
    @TableField("express_id")
    private Integer expressId;

    @Schema(description = "商品Id", example = "1")
    @TableField("product_id")
    private Integer productId;
}