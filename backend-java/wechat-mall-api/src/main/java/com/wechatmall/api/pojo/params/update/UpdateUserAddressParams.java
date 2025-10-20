package com.wechatmall.api.pojo.params.update;

import com.wechatmall.api.pojo.params.add.CreateUserAddressParams;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 更新用户地址请求DTO
 * 用于更新用户收货地址信息的请求参数封�? * 
 * @author WechatMall
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAddressParams extends CreateUserAddressParams {
    
    /**
     * 地址ID
     */
    @NotNull(message = "地址ID不能为空")
    private Integer id;
}
