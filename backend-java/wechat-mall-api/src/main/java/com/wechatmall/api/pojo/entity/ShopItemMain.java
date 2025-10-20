package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("shop_item_main")
@Schema(name = "ShopItemMain", description = "实体")
public class ShopItemMain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品表Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "商品类别Id", example = "1")
    private Integer belongId;

    @Schema(description = "商品描述地址", example = "1")
    private Integer addressId;
}