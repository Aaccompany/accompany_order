package com.accompany.order.service.shop;

import com.accompany.order.service.shop.dto.Shop;
import com.accompany.order.service.shop.dao.ShopMapper;
import com.accompany.order.service.shop.dao.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private ShopMapper shopMapper;

    /**
        * 获取店铺信息
     * @param id 店铺Id
     * @return 店铺信息
     */
    @Override
    public Shop findShopById(Long id) {
        return shopMapper.selectById(id);
    }
}
