package com.accompany.order.service.foot;

import com.accompany.order.config.SecurityInterceptor;
import com.accompany.order.controller.foot.footvo.FootAdminReqVo;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.foot.dao.FootMapper;
import com.accompany.order.service.foot.dao.IFootService;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class FootServiceImpl extends ServiceImpl<FootMapper, Foot> implements IFootService {

    @Autowired
    private FootMapper footMapper;

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Override
    public List<Foot> findAllFoot() {
        QueryWrapper<Foot> query = new QueryWrapper<>();
        query.orderByAsc("type_id")
            .lambda().eq(Foot::getIsDel,false);
        return footMapper.selectList(query);
    }

    @Override
    public Page<Foot> findAllFootAdmin(String name, int pageNum, int pageSize) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Page<Foot> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Foot> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            query.lambda().eq(Foot::getName,"%"+name+"%");
        }
        return footMapper.selectPage(page,query);
    }

    @Override
    public void addFoot(FootAdminReqVo footAdminReqVo) {
        User curUser = SecurityInterceptor.loginUser.get();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Foot foot = CommonUtils.genByCopyProperties(footAdminReqVo, Foot.class);
        Date now = new Date();
        foot.setIsDel(false);
        foot.setCreateTime(now);
        foot.setUpdateTime(now);
        foot.setCreateUser(curUser.getName());
        foot.setUpdateUser(curUser.getName());
    }

    @Override
    public void delFoot(Long footId) {
        User curUser = SecurityInterceptor.loginUser.get();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Foot foot = footMapper.selectById(footId);
        if (foot==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品不存在"));
        }
        foot.setIsDel(true);
        foot.setUpdateUser(curUser.getName());
        foot.setUpdateTime(new Date());
        saveOrUpdate(foot);
    }

    @Override
    public void updateFoot(Long footId, FootAdminReqVo footAdminReqVo) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Foot foot = footMapper.selectById(footId);
        if (foot==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("菜品不存在"));
        }
        BeanUtils.copyProperties(footAdminReqVo,foot);
        foot.setUpdateTime(new Date());
        foot.setUpdateUser(curUser.getName());
        saveOrUpdate(foot);
    }

}
