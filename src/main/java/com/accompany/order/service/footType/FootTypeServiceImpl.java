package com.accompany.order.service.footType;

import com.accompany.order.controller.foot.foottypevo.FootTypeAdminReqVo;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class FootTypeServiceImpl extends ServiceImpl<FootTypeMapper, FootType> implements IFootTypeService {

    @Autowired
    private FootTypeMapper footTypeMapper;

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Override
    public List<FootType> findAllFootType() {
        QueryWrapper<FootType> query = new QueryWrapper<>();
        query.orderByAsc("id")
        .lambda().eq(FootType::getIsDel,false);
        return footTypeMapper.selectList(query);
    }

    @Override
    public Page<FootType> findAllFootType(String name, int pageNum, int pageSize) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Page<FootType> page = new Page<>(pageNum,pageSize);
        QueryWrapper<FootType> query = new QueryWrapper<>();
        query.orderByAsc("id")
            .lambda().eq(FootType::getIsDel,false);
        return footTypeMapper.selectPage(page,query);
    }

    @Override
    public void addFootType(FootTypeAdminReqVo footTypeAdminReqVo) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Date now = new Date();
        FootType footType = CommonUtils.genByCopyProperties(footTypeAdminReqVo, FootType.class);
        footType.setIsDel(false);
        footType.setCreateTime(now);
        footType.setUpdateTime(now);
        footType.setCreateUser(curUser.getName());
        footType.setUpdateUser(curUser.getName());
        saveOrUpdate(footType);
    }

    @Override
    public void delFootType(Long footId) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        FootType footType = footTypeMapper.selectById(footId);
        if (footType==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品类别不存在"));
        }
        footType.setUpdateUser(curUser.getName());
        footType.setUpdateTime(new Date());
        footType.setIsDel(true);
        saveOrUpdate(footType);
    }

    @Override
    public FootType footTypeDetail(Long footId) {
        return footTypeMapper.selectById(footId);
    }

    @Override
    public void updateFootType(Long footId, FootTypeAdminReqVo footTypeAdminReqVo) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        FootType footType = footTypeMapper.selectById(footId);
        if (footType==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("菜品类别不存在"));
        }
        BeanUtils.copyProperties(footTypeAdminReqVo,footType);
        footType.setUpdateTime(new Date());
        footType.setUpdateUser(curUser.getName());
        saveOrUpdate(footType);
    }
}
