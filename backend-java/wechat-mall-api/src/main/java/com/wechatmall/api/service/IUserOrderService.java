package com.wechatmall.api.service;

import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.entity.UserOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechatmall.api.pojo.vo.UserOrderVo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
public interface IUserOrderService extends IService<UserOrder> {

    List<UserOrderVo> selectUserOrderByUserId(@NotNull(message = "用户ID不能为空") String userId);
}
