package com.accompany.order.service.shop.dao;

import com.accompany.order.service.shop.dto.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
public interface IShopService extends IService<Shop> {
    Shop findById(Long id);
}
