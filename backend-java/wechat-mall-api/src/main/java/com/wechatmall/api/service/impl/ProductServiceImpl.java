package com.wechatmall.api.service.impl;

import com.wechatmall.api.pojo.entity.Product;
import com.wechatmall.api.dao.ProductMapper;
import com.wechatmall.api.service.IProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
