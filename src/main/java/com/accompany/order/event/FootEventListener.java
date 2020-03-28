package com.accompany.order.event;

import com.accompany.order.config.CaffeineConfig;
import com.accompany.order.service.foot.dto.Foot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class FootEventListener {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CacheManager cacheManager;

    @EventListener
    @Async
    public void onDelFootEvent(DelFootEvent delFootEvent) {
        FootEventListener footEventListener = applicationContext.getBean("footEventListener", FootEventListener.class);
        footEventListener.delFootCache(delFootEvent.getFootId());
    }

    @EventListener
    @Async
    public void onUpdateFootEvent(UpdateFootEvent updateFootEvent) {
        FootEventListener footEventListener = applicationContext.getBean("footEventListener", FootEventListener.class);
        footEventListener.updateFootCache(updateFootEvent.getFoot());
    }

    @CacheEvict(value = "foot", key = "#footId")
    public void delFootCache(Long footId) {
        Cache cache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        List<Foot> footList = cache.get(CaffeineConfig.Caches.foot.name(), List.class);
        assert footList != null;
        Iterator<Foot> iterator = footList.iterator();
        while (iterator.hasNext()) {
            Foot foot = iterator.next();
            if (foot.getId().equals(footId)) {
                iterator.remove();
                break;
            }
        }
        log.info(String.format("清除本地缓存,footId: %s", footId));
    }

    @CachePut(value = "foot", key = "#foot.getId()")
    public Foot updateFootCache(Foot foot) {
        Cache cache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        List<Foot> footList = cache.get(CaffeineConfig.Caches.foot.name(), List.class);
        assert footList != null;
        Iterator<Foot> iterator = footList.iterator();
        while (iterator.hasNext()) {
            Foot item = iterator.next();
            if (foot.getId().equals(item.getId())) {
                iterator.remove();
                break;
            }
        }
        footList.add(foot);
        cache.put(CaffeineConfig.Caches.foot.name(), footList);
        log.info(String.format("修改本地缓存,footId: %s", foot.getId()));
        return foot;
    }

}
