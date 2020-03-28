package com.accompany.order.event;

import com.accompany.order.config.CaffeineConfig;
import com.accompany.order.service.footType.dto.FootType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * @author Accompany
 */
@Slf4j
@Component
public class FootTypeEventListener {

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private CacheManager cacheManager;

    @EventListener
    @Async
    public void onDelFootType(DelFootTypeEvent event) {
        FootTypeEventListener footTypeEventListener = applicationContext.getBean("footTypeEventListener", FootTypeEventListener.class);
        footTypeEventListener.removeFootTypeCache(event.getFootType().getId());
    }

    @EventListener
    @Async
    public void onUpdateFootType(UpdateFootTypeEvent event) {
        FootTypeEventListener footTypeEventListener = applicationContext.getBean("footTypeEventListener", FootTypeEventListener.class);
        footTypeEventListener.updateFootTypeCache(event.getFootType());
    }

    @CacheEvict(value = "footType", key = "#footTypeId")
    public void removeFootTypeCache(Long footTypeId) {
        //修改列表缓存数据
        Cache cache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        assert cache != null;
        List<FootType> footTypeList = cache.get("footType", List.class);
        assert footTypeList != null;
        Iterator<FootType> iterator = footTypeList.iterator();
        while (iterator.hasNext()) {
            FootType footType = iterator.next();
            if (footType.getId().equals(footTypeId)) {
                iterator.remove();
                break;
            }
        }
        log.info(String.format("清除本地缓存，footTypeId:%s", footTypeId));
    }

    @CachePut(value = "footType", key = "#footType.getId()")
    public FootType updateFootTypeCache(FootType footType) {
        Cache cache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        assert cache != null;
        List<FootType> footTypeList = cache.get(CaffeineConfig.Caches.footType.name(), List.class);
        assert footTypeList != null;
        Iterator<FootType> iterator = footTypeList.iterator();
        while (iterator.hasNext()) {
            FootType item = iterator.next();
            if (footType.getId().equals(item.getId())) {
                iterator.remove();
                break;
            }
        }
        footTypeList.add(footType);
        cache.put(CaffeineConfig.Caches.footType.name(),footTypeList);
        log.info(String.format("修改本地缓存，footTypeId:%s", footType.getId()));
        //表示修改
        return footType;
    }

}
