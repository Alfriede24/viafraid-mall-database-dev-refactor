package com.wechatmall.api.pojo.dto.miniapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序首页弹窗和轮播图信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BannerItemDTO", description = "小程序首页轮播或弹窗配置")
public class BannerItemDTO {
    @Schema(description = "唯一标识")
    private Long id;

    @Schema(description = "展示标题")
    private String title;

    @Schema(description = "展示图片地址")
    private String picUrl;

    @Schema(description = "点击后跳转的小程序路径或外链")
    private String targetPath;

    @Schema(description = "展示描述文案")
    private String description;

    @Schema(description = "是否以弹窗形式展示")
    private boolean popup;
}
