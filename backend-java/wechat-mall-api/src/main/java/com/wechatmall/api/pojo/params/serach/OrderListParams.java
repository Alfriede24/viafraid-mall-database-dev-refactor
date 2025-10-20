package com.wechatmall.api.pojo.params.serach;


import com.wechatmall.api.pojo.params.common.PageParams;
import io.swagger.v3.oas.annotations.media.DependentSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 订单查询参数
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/21 23:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderListParams extends PageParams implements Serializable {
   @Schema(description = "支付状态")
   private String paymentState;
   @Schema(description = "订单状态")
   private String state;
   @Schema(description = "商品ID")
   private String productId;
}
