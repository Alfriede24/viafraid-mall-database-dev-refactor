package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("user_information")
@Schema(name = "UserInformation", description = "实体")
public class UserInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @TableId("id")
    private Integer id;

    @Schema(description = "名称", example = "示例值")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "性别", example = "示例值")
    @TableField("sex")
    private String sex;

    @Schema(description = "电话", example = "1")
    @TableField("phone")
    private String phone;
}