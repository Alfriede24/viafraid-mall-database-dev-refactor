package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2025-10-15 22:15:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_order")
@Schema(name = "UserOrder", description = "实体")
public class UserOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;

    @Schema(description = "订单Id", example = "1")
    @TableField("order_id")
    private Integer orderId;

    @Schema(description = "用户Id", example = "1")
    @TableField("user_id")
    private Integer userId;

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
}