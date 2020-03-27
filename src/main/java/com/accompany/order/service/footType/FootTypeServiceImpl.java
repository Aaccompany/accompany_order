package com.accompany.order.service.footType;

import com.accompany.order.controller.foot.foottypevo.FootTypeAdminReqVo;
import com.accompany.order.event.DelFootTypeEvent;
import com.accompany.order.event.UpdateFootTypeEvent;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.footType.dao.FootTypeMapper;
import com.accompany.order.service.footType.dao.IFootTypeService;
import com.accompany.order.service.footType.dto.FootType;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
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
@Slf4j
public class FootTypeServiceImpl extends ServiceImpl<FootTypeMapper, FootType> implements IFootTypeService {

    @Resource
    private FootTypeMapper footTypeMapper;

    @Resource(name = "userServiceImpl")
    private IUserService userService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private ApplicationContext applicationContext;
    @PostConstruct
    public void init(){
        log.info("加载全部菜品类型");
        //加载全部菜品类型
        FootTypeServiceImpl footTypeService = applicationContext.getBean(FootTypeServiceImpl.class);
        Cache cache = cacheManager.getCache("footType");
        List<FootType> footTypeList = footTypeService.findAllFootType();
        for (FootType footType : footTypeList) {
            cache.put(footType.getId(),footType);
        }
    }
    /**
     * 查询店铺的全部菜品类别
     * @return 菜品类别数组
     */
    @Override
    public List<FootType> findAllFootType() {
        QueryWrapper<FootType> query = new QueryWrapper<>();
        query.orderByDesc("update_time")
        .lambda().eq(FootType::getIsDel,false);
        return footTypeMapper.selectList(query);
    }

    /**
     * 查询菜品类别分页列表
     * @param name 模糊查询菜品类别名称
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Override
    public Page<FootType> findAllFootType(String name, int pageNum, int pageSize) {
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //根据条件获取菜品类别列表
        Page<FootType> page = new Page<>(pageNum,pageSize);
        QueryWrapper<FootType> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            query.lambda().like(FootType::getName,"%"+name+"%");
        }
        query.orderByAsc("id")
            .lambda().eq(FootType::getIsDel,false);
        return footTypeMapper.selectPage(page,query);
    }

    /**
     * 添加菜品类别
     * @param footTypeAdminReqVo 需要添加的菜品类别信息对象
     */
    @Override
    public void addFootType(FootTypeAdminReqVo footTypeAdminReqVo) {
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Date now = new Date();
        FootType footType = CommonUtils.genByCopyProperties(footTypeAdminReqVo, FootType.class);
        footType.setIsDel(false);
        footType.setCreateTime(now);
        footType.setUpdateTime(now);
        footType.setCreateUser(curUser.getId());
        footType.setUpdateUser(curUser.getId());
        //保存菜品类别
        saveOrUpdate(footType);
    }

    /**
     * 删除菜品类别
     * @param footTypeId 需要删除的菜品类别id
     */
    @Override
    public void delFootType(Long footTypeId) {
        //获取当前操作用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //判断待删除菜品类别是否存在
        FootType footType = footTypeMapper.selectById(footTypeId);
        if (footType==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品类别不存在"));
        }
        footType.setUpdateUser(curUser.getId());
        footType.setUpdateTime(new Date());
        footType.setIsDel(true);
        applicationEventPublisher.publishEvent(new DelFootTypeEvent(this,footType));
        //删除菜品类别
        saveOrUpdate(footType);
    }

    /**
     * 菜品类别详情
     * @param footTypeId 需要查询的菜品类别id
     * @return 菜品类别对象
     */
    @Override
    @Cacheable(value = "footType",key="#footTypeId")
    public FootType footTypeDetail(Long footTypeId) {

        //通过{footTypeId}获取菜品类别
        return footTypeMapper.selectById(footTypeId);
    }

    /**
     * 修改菜品类别
     * @param footTypeId 菜品类别id
     * @param footTypeAdminReqVo 菜品类别基础信息类
     */
    @Override
    public void updateFootType(Long footTypeId, FootTypeAdminReqVo footTypeAdminReqVo) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断是否具有操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        FootType footType = footTypeMapper.selectById(footTypeId);
        if (footType==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品类别不存在"));
        }
        BeanUtils.copyProperties(footTypeAdminReqVo,footType);
        footType.setUpdateTime(new Date());
        footType.setUpdateUser(curUser.getId());
        applicationEventPublisher.publishEvent(new UpdateFootTypeEvent(this,footType));
        //修改菜品类别
        saveOrUpdate(footType);
    }
}
