package com.wechatmall.api.service.impl;

import com.wechatmall.api.pojo.entity.User;
import com.wechatmall.api.dao.UserMapper;
import com.wechatmall.api.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
