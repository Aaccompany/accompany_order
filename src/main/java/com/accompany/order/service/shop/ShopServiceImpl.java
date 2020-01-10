package com.accompany.order.service.shop;

import com.accompany.order.service.shop.dto.Shop;
import com.accompany.order.service.shop.dao.ShopMapper;
import com.accompany.order.service.shop.dao.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Override
    public Shop findById(Long id) {
        return findById(id);
    }
}
