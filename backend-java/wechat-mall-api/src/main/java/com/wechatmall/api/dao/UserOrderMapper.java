package com.wechatmall.api.dao;

import com.wechatmall.api.pojo.entity.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechatmall.api.pojo.vo.UserOrderVo;
import com.wechatmall.api.service.impl.UserOrderServiceImpl;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
public interface UserOrderMapper extends BaseMapper<UserOrder> {
    /**
     * 根据用户ID查询用户订单信息
     * @param userId 用户Id
     * @return 用户订单信息
     */
    List<UserOrderVo> selectUserOrderByUserId(String userId);
}
