package com.wechatmall.api.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechatmall.api.pojo.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechatmall.api.pojo.vo.OrdersVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:58:04
 */
public interface IOrdersService extends IService<Orders> {
    List<OrdersVo> selectOrderByUserId(Integer userId);
    Page<OrdersVo> selectOrderByPage(Page<OrdersVo> page, Wrapper<Orders> wrapper);
    List<OrdersVo> selectOrderByList(Wrapper<Orders> wrapper);
}
