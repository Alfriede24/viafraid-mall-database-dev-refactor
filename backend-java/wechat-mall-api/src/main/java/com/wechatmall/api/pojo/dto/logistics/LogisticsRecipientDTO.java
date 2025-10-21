package com.wechatmall.api.pojo.dto.logistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收件人信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LogisticsRecipientDTO", description = "物流收件人信息")
public class LogisticsRecipientDTO {
    @Schema(description = "收件人姓名")
    private String name;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "收货地址")
    private String address;
}
