package com.wechatmall.api.pojo.dto;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 通用分页DTO
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/22 00:33
 */
@Data
public class PageDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页数")
    private long pageNum;
    @Schema(description = "每页大小")
    private long pageSize;
    @Schema(description = "总数")
    private long total;
    @Schema(description = "具体数据")
    private List<T> list;

    // 使用默认BeanUti转换
    public static <PO,VO> PageDto<VO> of(IPage<PO> entity, Class<VO> clazz){
        PageDto<VO> pageDto = new PageDto<>();
        // 设置总数
        pageDto.setTotal(entity.getTotal());
        // 设置当前页数
        pageDto.setPageNum(entity.getCurrent());
        // 当前页大小
        pageDto.setPageSize(entity.getSize());
        // 数据项填充
        List<PO> records = entity.getRecords();
        if(CollUtil.isEmpty(records)){
            pageDto.setList(Collections.emptyList());
            return pageDto;
        }
        // 拷贝数据
        pageDto.setList(BeanUtil.copyToList(records,clazz));
        return pageDto;
    }

    // 使用转换器
    public static <PO,VO> PageDto<VO> of(IPage<PO> entity, Function<PO, VO> function){
        PageDto<VO> pageDto = new PageDto<>();
        // 设置总数
        pageDto.setTotal(entity.getTotal());
        // 设置当前页数
        pageDto.setPageNum(entity.getCurrent());
        // 当前页大小
        pageDto.setPageSize(entity.getSize());
        // 数据项填充
        List<PO> records = entity.getRecords();
        if(CollUtil.isEmpty(records)){
            pageDto.setList(Collections.emptyList());
            return pageDto;
        }
        // 拷贝数据
        pageDto.setList(records.stream().map(function).collect(Collectors.toList()));
        return pageDto;
    }
}
