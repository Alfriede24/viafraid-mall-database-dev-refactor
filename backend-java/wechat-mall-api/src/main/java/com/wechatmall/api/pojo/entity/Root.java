package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@Schema(name = "Root", description = "实体")
public class Root implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "后台Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "rootId", type = IdType.AUTO)
    private Long rootId;

    @Schema(description = "后台用户名", example = "示例值")
    @TableField("username")
    private String username;

    @Schema(description = "后台密码", example = "示例值")
    @TableField("`password`")
    private String password;

    @Schema(description = "用户权限 1 , 2 , 3 , 4", example = "1")
    @TableField("auth")
    private Integer auth;
}