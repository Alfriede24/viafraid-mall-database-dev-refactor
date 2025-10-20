package com.wechatmall.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechatmall.api.pojo.entity.Orders;
import com.wechatmall.api.dao.OrdersMapper;
import com.wechatmall.api.pojo.vo.OrdersVo;
import com.wechatmall.api.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:58:04
 */
@Service
@AllArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    private OrdersMapper ordersMapper;

    @Override
    public List<OrdersVo> selectOrderByUserId(Integer userId) {
        return ordersMapper.selectOrderByUserId(userId);
    }

    @Override
    public Page<OrdersVo> selectOrderByPage(Page<OrdersVo> page, Wrapper<Orders> wrapper) {
        return ordersMapper.selectOrderVoPage(page,wrapper);
    }

    @Override
    public List<OrdersVo> selectOrderByList(Wrapper<Orders> wrapper) {
        return ordersMapper.selectOrderVoList(wrapper);
    }

}
