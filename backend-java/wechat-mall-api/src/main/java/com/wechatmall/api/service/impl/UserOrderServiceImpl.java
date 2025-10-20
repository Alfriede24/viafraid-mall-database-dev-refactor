package com.wechatmall.api.service.impl;

import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.entity.UserOrder;
import com.wechatmall.api.dao.UserOrderMapper;
import com.wechatmall.api.pojo.vo.UserOrderVo;
import com.wechatmall.api.service.IUserOrderService;
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
 * @since 2025-10-15 22:15:48
 */
@Service
@AllArgsConstructor
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements IUserOrderService {

    private final UserOrderMapper userOrderMapper;

    @Override
    public List<UserOrderVo> selectUserOrderByUserId(String userId) {
        return userOrderMapper.selectUserOrderByUserId(userId);
    }
}
