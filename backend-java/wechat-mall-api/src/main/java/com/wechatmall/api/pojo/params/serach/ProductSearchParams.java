package com.wechatmall.api.pojo.params.serach;


import com.wechatmall.api.pojo.params.common.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 请求参数信息
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/21 22:46
 */
@Schema(description = "查询参数")
public class ProductSearchParams extends PageParams {
    /**
     * 搜索关键词
     */
    @Schema(description = "搜索关键字")
    private String keyword;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 是否推荐
     */
    private Boolean isRecommended;

    /**
     * 是否新品
     */
    private Boolean isNew;

    /**
     * 是否热销
     */
    private Boolean isHot;

    /**
     * 最低价�?     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 商品状态
     */
    private Integer status;
}
