package com.accompany.order.service.shop;

import com.accompany.order.config.CaffeineConfig;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.shop.dto.Shop;
import com.accompany.order.service.shop.dao.ShopMapper;
import com.accompany.order.service.shop.dao.IShopService;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

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


    private volatile Shop shop;

    @PostConstruct
    public void initShop(){
        shop = findShopById();
    }

    /**
     *  获取店铺信息
     * @return 店铺信息
     */
    @Override
    public Shop findShopById() {
        //采用双重锁初始化
        if (shop==null){
            //说明没有加载
            synchronized (Shop.class){
                if (shop==null){
                    List<Shop> shopList = shopMapper.selectList(null);
                    if (CollectionUtils.isEmpty(shopList)){
                        throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("店铺数据异常，请联系管理员操作"));
                    }
                    shop = shopList.get(0);
                }
            }
        }
        return shop;
    }
}
