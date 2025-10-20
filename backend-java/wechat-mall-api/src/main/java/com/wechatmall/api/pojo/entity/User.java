package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name = "User", description = "实体")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "user唯一Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名", example = "示例值")
    @TableField("username")
    private String username;

    @Schema(description = "登录时间", example = "示例值")
    @TableField("login_time")
    private String loginTime;

    @Schema(description = "登录权限", example = "1")
    @TableField("is_disabled")
    private Integer isDisabled;

    @Schema(description = "微信授权ID", example = "示例值")
    @TableField("open_id")
    private String openId;
}