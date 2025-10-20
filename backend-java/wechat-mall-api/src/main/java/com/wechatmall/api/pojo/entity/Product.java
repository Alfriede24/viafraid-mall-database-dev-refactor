package com.wechatmall.api.pojo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@Schema(name = "Product", description = "实体")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @Schema(description = "类型Id", example = "1")
    @TableField("type_id")
    private Integer typeId;

    @Schema(description = "主图片地址", example = "示例值")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "库存", example = "示例值")
    @TableField("store_count")
    private String storeCount;

    @Schema(description = "当前价格", example = "示例值")
    @TableField("price")
    private String price;

    @Schema(description = "原价", example = "示例值")
    @TableField("origin_price")
    private String originPrice;

    @Schema(description = "售出数量", example = "示例值")
    @TableField("sales_count")
    private String salesCount;
}