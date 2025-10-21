package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 会员资料更新请求
 */
@Data
@Schema(name = "MiniAppMemberProfileUpdateRequest", description = "会员资料更新入参")
public class MiniAppMemberProfileUpdateRequest {

    @Schema(description = "昵称", example = "张三")
    @Size(max = 32, message = "昵称长度不能超过32个字符")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "手机号", example = "13812345678")
    private String phone;

    @Schema(description = "性别(0未知 1男 2女)")
    private Integer gender;
}
