package com.wechatmall.api.pojo.dto.express;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可选快递公司
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExpressCompanyDTO", description = "快递公司信息")
public class ExpressCompanyDTO {
    @Schema(description = "公司名称")
    private String name;

    @Schema(description = "快递100编码")
    private String code;
}
