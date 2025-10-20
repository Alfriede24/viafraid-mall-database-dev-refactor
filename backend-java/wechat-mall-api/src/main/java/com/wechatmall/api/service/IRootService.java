package com.wechatmall.api.service;

import com.wechatmall.api.pojo.dto.RootDto;
import com.wechatmall.api.pojo.entity.Root;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
public interface IRootService extends IService<Root> {
    /**
     * 后台登录服务
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username, String password);

    /**
     * 注册服务
     *
     * @param userDto 用户信息
     * @return 注册结果
     */
    String register(RootDto userDto);
}
