package com.wechatmall.api.pojo.params.common;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: list查询参数分页信息
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/21 22:47
 */
@Data
@Tag(name="分页参数",description = "分页查询参数")
public class PageParams implements Serializable {
    /**
     * 页码
     */
    @Schema(description = "页码")
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小")
    private Integer pageSize = 20;


    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序类型: false: 降序 true：升序 默认升序
     */
    @Schema(description = "是否升序")
    private Boolean isAsc=true;

    // 分页参数转换
    public <T> Page<T> toMapPage(OrderItem ... items){
        Page<T> pageParam = Page.of(page,pageSize);
        if(StrUtil.isNotBlank(sortField)){
            // 排序字段不为空
            pageParam.addOrder(isAsc ? OrderItem.asc(sortField) : OrderItem.desc(sortField));
        }else if(items != null) {
            // 排序字段为空使用默认字段
            pageParam.addOrder(Arrays.asList(items));
        }
        return pageParam;
    }

    // 默认根据创建时间排序
    public <T> Page<T> toMapPageDefaultSortByCreateTime(){
        return toMapPage(OrderItem.asc("createTime"));
    }

    // 默认根据更新时间排序
    public <T> Page<T> toMapPageDefaultSortByUpdateTime(){
        return toMapPage(OrderItem.desc("updateTime"));
    }




}
