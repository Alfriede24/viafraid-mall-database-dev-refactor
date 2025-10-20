package com.wechatmall.api.pojo.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wechatmall.api.pojo.entity.Orders;
import com.wechatmall.api.pojo.entity.UserOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 用户订单Vo
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/10/19 10:07
 */
@Data
public class UserOrderVo implements Serializable {
    @Schema(description = "唯一Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id")
    private Integer id;

    @Schema(description = "更新日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "是否支付", example = "1")
    @TableField("is_pay")
    private Integer isPay;

    @Schema(description = "商品描述", example = "示例值")
    @TableField("`description`")
    private String description;

    @Schema(description = "照片地址", example = "示例值")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "订单列表")
    List<OrdersVo> orders;

    @Schema(description = "用户名")
    private String username;
}
