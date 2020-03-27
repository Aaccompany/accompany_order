package com.accompany.order.event;

import com.accompany.order.service.footType.dto.FootType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Accompany
 */
@Slf4j
@Component
public class FootTypeEventListener {

    @Resource
    private ApplicationContext applicationContext;

    @EventListener
    @Async
    public void onDelFootType(DelFootTypeEvent event){
        applicationContext.getBean("FootTypeEventListener",FootTypeEventListener.class).removeFootTypeCache(event.getFootType().getId());
    }

    @EventListener
    @Async
    public void onUpdateFootType(UpdateFootTypeEvent event){
        applicationContext.getBean("FootTypeEventListener",FootTypeEventListener.class).updateFootTypeCache(event.getFootType());
    }


    @CacheEvict(value = "footType",key = "#footTypeId")
    public void removeFootTypeCache(Long footTypeId){
        log.info(String.format("清除本地缓存，footTypeId:%s",footTypeId));
    }

    @CachePut(value = "footType",key = "#footType.getId()")
    public FootType updateFootTypeCache(FootType footType){
        log.info(String.format("修改本地缓存，footTypeId:%s",footType.getId()));
        return footType;
    }

}
