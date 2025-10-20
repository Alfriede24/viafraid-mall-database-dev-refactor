package com.wechatmall.api.pojo.entity;

import java.io.Serializable;

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
 * @since 2025-10-15 22:15:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Express", description = "实体")
public class Express implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @Schema(description = "快递单号", example = "1")
    @TableField("express_code")
    private Integer expressCode;

    @Schema(description = "对应订单Id", example = "1")
    @TableField("order_id")
    private Integer orderId;

    @Schema(description = "状态", example = "示例值")
    @TableField("state")
    private String state;

    @Schema(description = "对应地址Id", example = "1")
    @TableField("address_id")
    private Integer addressId;

    @Schema(description = "查询地址接口", example = "示例值")
    @TableField("express_url")
    private String expressUrl;
}