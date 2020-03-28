package com.accompany.order.service.foot;

import com.accompany.order.config.CaffeineConfig;
import com.accompany.order.config.SecurityInterceptor;
import com.accompany.order.controller.foot.footvo.FootAdminReqVo;
import com.accompany.order.event.DelFootEvent;
import com.accompany.order.event.UpdateFootEvent;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.foot.dao.FootMapper;
import com.accompany.order.service.foot.dao.IFootService;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.foot.dto.FootCountOnMonth;
import com.accompany.order.service.footType.FootTypeServiceImpl;
import com.accompany.order.service.footType.dto.FootType;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Service
@Slf4j
public class FootServiceImpl extends ServiceImpl<FootMapper, Foot> implements IFootService {

    @Resource
    private FootMapper footMapper;

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void init(){
        log.info("加载全部菜品...");
        FootServiceImpl footService = applicationContext.getBean(FootServiceImpl.class);
        Cache footTypeCache = cacheManager.getCache(CaffeineConfig.Caches.foot.name());
        List<Foot> footList = footService.findAllFoot();
        for (Foot foot : footList) {
            footTypeCache.put(foot.getId(),foot);
        }
        Cache allFootTypeCache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        allFootTypeCache.put(CaffeineConfig.Caches.foot.name(),footList);
    }

    /**
     * 查询全部菜品
     * @return 菜品数组
     */
    @Override
    @Cacheable(value = "listCache",key = "'foot'")
    public List<Foot> findAllFoot() {
        QueryWrapper<Foot> query = new QueryWrapper<>();
        //查询所有未删除菜品并且根据type_id进行升序排序，
        query.orderByAsc("type_id")
            .lambda().eq(Foot::getIsDel,false);
        //返回查询结果
        return footMapper.selectList(query);
    }

    /**
     * 查询菜品分页列表
     * @param name 模糊查询菜品名称
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Override
    public Page<Foot> findAllFootAdmin(String name, int pageNum, int pageSize) {
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //根据条件获取菜品列表
        Page<Foot> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Foot> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            query.lambda().like(Foot::getName,"%"+name+"%");
        }
        query.lambda().eq(Foot::getIsDel,false);
        return footMapper.selectPage(page,query);
    }

    /**
     * 添加菜品
     * @param footAdminReqVo 菜品基础信息对象
     */
    @Override
    public void addFoot(FootAdminReqVo footAdminReqVo) {
        //获取当前操作用户信息
        User curUser = SecurityInterceptor.loginUser.get();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //初始化菜品基础信息
        Foot foot = CommonUtils.genByCopyProperties(footAdminReqVo, Foot.class);
        Date now = new Date();
        foot.setIsDel(false);
        foot.setCreateTime(now);
        foot.setUpdateTime(now);
        foot.setCreateUser(curUser.getId());
        foot.setUpdateUser(curUser.getId());
        //添加菜品
        footMapper.insert(foot);
        applicationEventPublisher.publishEvent(new UpdateFootEvent(this,foot));
    }

    /**
     * 删除菜品
     * @param footId 需要删除的菜品id
     */
    @Override
    public void delFoot(Long footId) {
        //获取当前操作用户信息
        User curUser = SecurityInterceptor.loginUser.get();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //判断传递过来的菜品是否存在
        Foot foot = footMapper.selectById(footId);
        if (foot==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品不存在"));
        }
        foot.setIsDel(true);
        foot.setUpdateUser(curUser.getId());
        foot.setUpdateTime(new Date());
        applicationEventPublisher.publishEvent(new DelFootEvent(this,foot.getId()));
        //删除菜品
        saveOrUpdate(foot);
    }

    /**
     * 修改菜品
     * @param footId 需要修改的菜品id
     * @param footAdminReqVo 需要修改的菜品信息对象
     */
    @Override
    public void updateFoot(Long footId, FootAdminReqVo footAdminReqVo) {
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //判断传递过来的菜品是否存在
        Foot foot = footMapper.selectById(footId);
        if (foot==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("菜品不存在"));
        }
        BeanUtils.copyProperties(footAdminReqVo,foot);
        foot.setUpdateTime(new Date());
        foot.setUpdateUser(curUser.getId());
        applicationEventPublisher.publishEvent(new UpdateFootEvent(this,foot));
        //更新菜品信息
        saveOrUpdate(foot);
    }

    @Override
    public List<Foot> findByIds(List<Long> ids) {
        Cache cache = cacheManager.getCache(CaffeineConfig.Caches.listCache.name());
        List<Foot> footList = cache.get(CaffeineConfig.Caches.foot.name(), List.class);
        Map<Long, Foot> footMap = footList.stream().collect(Collectors.toMap(Foot::getId, item -> item));
        List<Foot> result = Lists.newArrayList();
        for (Long id : ids) {
            Foot foot = footMap.get(id);
            if (foot==null){
                return footMapper.selectBatchIds(ids);
            }
            result.add(foot);
        }
        return result;
    }

    @Override
    public List<FootCountOnMonth> findCountOnMonth(int month) {
        if (month<=0||month>12){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("月份参数不合法，请检查后重新输入"));
        }
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        List<FootCountOnMonth> countOnMonth = footMapper.findCountOnMonth(month);
        int allCount = footMapper.footCountOnMonth(month);
        int count = countOnMonth.stream().mapToInt(FootCountOnMonth::getCount).sum();
        countOnMonth.add(new FootCountOnMonth(allCount-count,"其他",-1L));
        return countOnMonth;
    }


}
