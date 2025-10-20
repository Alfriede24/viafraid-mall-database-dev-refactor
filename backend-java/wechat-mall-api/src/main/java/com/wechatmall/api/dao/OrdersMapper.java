package com.wechatmall.api.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechatmall.api.pojo.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechatmall.api.pojo.vo.OrdersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:58:04
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    List<OrdersVo> selectOrderByUserId(Integer userId);
    List<OrdersVo> selectOrderVoList(@Param("ew") Wrapper<Orders> wrapper);
    Page<OrdersVo> selectOrderVoPage(Page<OrdersVo> page,@Param("ew") Wrapper<Orders> wrapper);
}
