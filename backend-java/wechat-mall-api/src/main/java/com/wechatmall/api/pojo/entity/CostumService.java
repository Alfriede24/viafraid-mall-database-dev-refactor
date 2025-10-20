package com.wechatmall.api.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("costum_service")
@Schema(name = "CostumService", description = "实体")
public class CostumService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客服单元Id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer id;

    @Schema(description = "客服唯一Id", example = "1")
    private Integer uniqueId;

    @Schema(description = "客服绩效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime workTime;
}