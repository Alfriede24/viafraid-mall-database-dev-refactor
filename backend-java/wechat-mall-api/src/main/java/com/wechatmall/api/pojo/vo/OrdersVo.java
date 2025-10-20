package com.wechatmall.api.pojo.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wechatmall.api.pojo.entity.Address;
import com.wechatmall.api.pojo.entity.Express;
import com.wechatmall.api.pojo.entity.Orders;
import com.wechatmall.api.pojo.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 订单Vo
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/10/19 10:05
 */
@Data
public class OrdersVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id")
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

    @Schema(description = "地址信息")
    private Address address;

    @Schema(description = "快递信息")
    private Express express;

    @Schema(description = "商品信息")
    private Product product;
   
}
