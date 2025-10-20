package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小程序端会员资料
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MiniAppMemberProfileDTO", description = "小程序端会员资料概览")
public class MiniAppMemberProfileDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "绑定手机号")
    private String phone;

    @Schema(description = "性别(0未知 1男 2女)")
    private Integer gender;

    @Schema(description = "性别描述")
    private String genderLabel;

    @Schema(description = "会员等级")
    private String level;

    @Schema(description = "加入时间")
    private String joinDate;

    @Schema(description = "会员标签")
    private List<String> tags;
}
