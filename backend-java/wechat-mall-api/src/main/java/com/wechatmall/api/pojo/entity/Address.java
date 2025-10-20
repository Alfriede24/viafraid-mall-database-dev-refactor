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
@Schema(name = "Address", description = "实体")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "地址id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;

    @Schema(description = "有地址", example = "1")
    @TableField("has_address")
    private Integer hasAddress;

    @Schema(description = "省", example = "示例值")
    @TableField("province")
    private String province;

    @Schema(description = "市", example = "示例值")
    @TableField("city")
    private String city;

    @Schema(description = "区", example = "示例值")
    @TableField("district")
    private String district;

    @Schema(description = "村镇", example = "示例值")
    @TableField("town")
    private String town;

    @Schema(description = "具体信息", example = "示例值")
    @TableField("main_address")
    private String mainAddress;

    @Schema(description = "备注",example = "示例值")
    @TableField("remark")
    private String remark;
}